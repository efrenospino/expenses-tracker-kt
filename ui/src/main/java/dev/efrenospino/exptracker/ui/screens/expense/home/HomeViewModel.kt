package dev.efrenospino.exptracker.ui.screens.expense.home

import androidx.lifecycle.viewModelScope
import dev.efrenospino.exptracker.domain.usecases.FilterExpensesByMonth
import dev.efrenospino.exptracker.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.ui.nav.AppNavigator
import dev.efrenospino.exptracker.ui.nav.Destination
import dev.efrenospino.exptracker.ui.nav.SimpleViewModel
import dev.efrenospino.exptracker.ui.utils.nextMonth
import dev.efrenospino.exptracker.ui.utils.previousMonth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Month

class HomeViewModel(
    private val filterExpensesByMonth: FilterExpensesByMonth,
    private val sortExpensesList: SortExpensesList,
    appNavigator: AppNavigator,
) : SimpleViewModel<HomeScreenState, Event>(
    appNavigator = appNavigator,
    viewModelState = MutableStateFlow(HomeScreenState())
) {

    override fun onEvent(event: Event, coroutineDispatcher: CoroutineDispatcher) {
        when (event) {
            Event.OnLoadAllExpenses -> onLoadMonthExpenses(coroutineDispatcher)
            Event.OnRegisterNewExpenseButtonClicked -> onRegisterNewExpenseButtonClicked()
            Event.OnGoToPreviousMonthButtonClicked -> onGoToPreviousMonth(coroutineDispatcher)
            Event.OnGoToNextMonthButtonClicked -> onGoToNextMonth(coroutineDispatcher)
            Event.OnGoToCurrentMonthButtonClicked -> onGoToCurrentMonthAndYear(coroutineDispatcher)
            is Event.OnExpenseRecordClicked -> onExpenseRecordClicked(event.expense)
            is Event.OnOrderByOptionChanged -> onOrderByOptionChanged(event.orderDescending)
            is Event.OnSortByOptionChanged -> onSortByOptionChanged(event.sortBy)
            is Event.OnYearMonthSetButtonClicked -> onYearMonthSetButtonClicked(coroutineDispatcher, event.month, event.year)
        }
    }

    private fun onYearMonthSetButtonClicked(dispatcher: CoroutineDispatcher = Dispatchers.IO, month: Month, year: Int) {
        viewModelState.update {
            it.copy(
                selectedMonth = month,
                selectedYear = year,
            )
        }
        onLoadMonthExpenses(dispatcher)
    }

    private fun onOrderByOptionChanged(orderByDescending: Boolean) {
        viewModelState.update {
            it.copy(
                orderByDescending = orderByDescending,
                expensesList = sortExpensesList(
                    SortExpensesList.Args(
                        expensesList = viewModelState.value.expensesList,
                        sortBy = viewModelState.value.sortBy,
                        orderDescending = orderByDescending
                    )
                )
            )
        }
    }

    private fun onSortByOptionChanged(sortBy: SortExpensesList.SortBy) {
        viewModelState.update {
            it.copy(
                sortBy = sortBy,
                expensesList = sortExpensesList(
                    SortExpensesList.Args(
                        expensesList = viewModelState.value.expensesList,
                        sortBy = sortBy,
                        orderDescending = viewModelState.value.orderByDescending
                    )
                )
            )
        }
    }

    private fun onGoToCurrentMonthAndYear(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelState.update {
            it.copy(
                selectedMonth = it.currentMonth,
                selectedYear = it.currentYear,
            )
        }
        onLoadMonthExpenses(dispatcher)
    }

    private fun onGoToPreviousMonth(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        val (selectedMonth, selectedYear) = previousMonth(
            viewModelState.value.selectedMonth,
            viewModelState.value.selectedYear
        )

        viewModelState.update {
            it.copy(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear
            )
        }

        onLoadMonthExpenses(dispatcher)
    }

    private fun onGoToNextMonth(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        val (selectedMonth, selectedYear) = nextMonth(
            viewModelState.value.selectedMonth,
            viewModelState.value.selectedYear
        )

        viewModelState.update {
            it.copy(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear
            )
        }

        onLoadMonthExpenses(dispatcher)
    }

    private fun onExpenseRecordClicked(expense: Expense) {
        appNavigator.tryNavigateTo(Destination.EditExpenseDestination(expense.id))
    }

    private fun onRegisterNewExpenseButtonClicked() {
        appNavigator.tryNavigateTo(Destination.NewExpenseDestination())
    }

    private fun onLoadMonthExpenses(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            filterExpensesByMonth(
                dispatcher,
                FilterExpensesByMonth.Args(
                    uiState.value.selectedMonth,
                    uiState.value.selectedYear
                )
            ).catch { throwable ->
                viewModelState.update {
                    it.copy(error = throwable)
                }
            }.collect { data ->
                viewModelState.update {
                    it.copy(
                        expensesList = sortExpensesList(
                            SortExpensesList.Args(
                                expensesList = data,
                                sortBy = viewModelState.value.sortBy,
                                orderDescending = viewModelState.value.orderByDescending
                            )
                        ), error = null
                    )
                }
            }
        }
    }

}
