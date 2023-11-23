package dev.efrenospino.ui.screens.expense.home

import androidx.lifecycle.viewModelScope
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.data.repositories.ExpensesRepository
import dev.efrenospino.ui.nav.AppNavigator
import dev.efrenospino.ui.nav.Destination
import dev.efrenospino.ui.nav.SimpleViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class HomeViewModel(
    private val expensesRepository: ExpensesRepository,
    appNavigator: AppNavigator,
) : SimpleViewModel<HomeScreenState, Event>(
    appNavigator = appNavigator,
    viewModelState = MutableStateFlow(
        HomeScreenState(
            expensesList = emptyList(),
            thisMonth = ZonedDateTime.now().month,
            error = null,
        )
    )
) {

    override fun onEvent(event: Event, coroutineDispatcher: CoroutineDispatcher) {
        when (event) {
            Event.OnLoadAllExpenses -> onLoadAllExpenses(coroutineDispatcher)
            Event.OnRegisterNewExpenseButtonClicked -> onRegisterNewExpenseButtonClicked()
            is Event.OnExpenseRecordClicked -> onExpenseRecordClicked(event.expense)
        }
    }

    private fun onExpenseRecordClicked(expense: Expense) {
        appNavigator.tryNavigateTo(Destination.EditExpenseDestination(expense.id))
    }

    private fun onRegisterNewExpenseButtonClicked() {
        appNavigator.tryNavigateTo(Destination.NewExpenseDestination())
    }

    private fun onLoadAllExpenses(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            expensesRepository.getAllExpenses(dispatcher).catch { throwable ->
                viewModelState.update {
                    it.copy(error = throwable)
                }
            }.collect { data ->
                viewModelState.update {
                    it.copy(expensesList = data, error = null)
                }
            }
        }
    }

}
