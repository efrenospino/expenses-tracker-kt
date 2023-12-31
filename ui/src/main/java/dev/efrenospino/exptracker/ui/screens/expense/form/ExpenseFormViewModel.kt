package dev.efrenospino.exptracker.ui.screens.expense.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.data.repositories.ExpensesRepository
import dev.efrenospino.exptracker.ui.nav.AppNavigator
import dev.efrenospino.exptracker.ui.nav.Destination
import dev.efrenospino.exptracker.ui.nav.SimpleViewModel
import dev.efrenospino.exptracker.ui.utils.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID

class ExpenseFormViewModel(
    private val expensesRepository: ExpensesRepository,
    appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SimpleViewModel<ExpenseFormScreenState, Event>(
    appNavigator = appNavigator,
    viewModelState = MutableStateFlow(
        ExpenseFormScreenState(
            expenseId = savedStateHandle.get<String>(Destination.ARG_EXPENSE_ID),
            expense = null,
            message = null,
        )
    )
) {

    init {
        if (viewModelState.value.expenseId != null) {
            onEvent(Event.OnLoadExpenseData, coroutineDispatcher)
        }
    }

    override fun onEvent(event: Event, coroutineDispatcher: CoroutineDispatcher) {
        when (event) {
            Event.OnNavigateBack -> onNavigateBack()
            Event.OnResetErrors -> onResetErrors()
            Event.OnLoadExpenseData -> onLoadExpenseData(coroutineDispatcher)
            is Event.OnSaveButtonClicked -> onSaveExpenseData(
                event.expenseAmount,
                event.expenseSummary,
                event.expenseDate,
                coroutineDispatcher
            )
        }
    }

    private fun onResetErrors() {
        viewModelState.update {
            it.copy(
                message = null
            )
        }
    }

    private fun onLoadExpenseData(coroutineDispatcher: CoroutineDispatcher) {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                expensesRepository.getExpenseById(expenseId = viewModelState.value.expenseId!!)
            }.onSuccess { data ->
                viewModelState.update {
                    it.copy(expense = data)
                }
            }.onFailure { throwable ->
                viewModelState.update {
                    it.copy(message = Message.Error.ExpenseDataNotLoaded(throwable))
                }
            }
        }
    }

    private fun onSaveExpenseData(
        expenseAmount: String,
        expenseSummary: String,
        expenseDate: ZonedDateTime,
        coroutineDispatcher: CoroutineDispatcher,
    ) {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                expensesRepository.save(
                    Expense(
                        id = viewModelState.value.expenseId ?: UUID.randomUUID().toString(),
                        summary = expenseSummary,
                        amount = expenseAmount.toDouble(),
                        createdAt = expenseDate,
                    )
                )
            }.onSuccess {
                viewModelState.update {
                    it.copy(message = Message.Info.ExpenseSaved)
                }
                appNavigator.tryNavigateBack()
            }.onFailure { throwable ->
                viewModelState.update {
                    it.copy(message = Message.Error.ExpenseNotSaved(throwable))
                }
            }
        }
    }
}

