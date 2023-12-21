package dev.efrenospino.exptracker.ui.screens.expense.home

import dev.efrenospino.exptracker.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.models.Expense
import java.time.Month

sealed class Event {
    data object OnLoadAllExpenses : Event()
    data class OnSortByOptionChanged(val sortBy: SortExpensesList.SortBy) : Event()
    data class OnOrderByOptionChanged(val orderDescending: Boolean) : Event()
    data object OnRegisterNewExpenseButtonClicked : Event()
    data class OnExpenseRecordClicked(val expense: Expense) : Event()
    data object OnGoToPreviousMonthButtonClicked : Event()
    data object OnGoToNextMonthButtonClicked : Event()
    data object OnGoToCurrentMonthButtonClicked : Event()
    data class OnYearMonthSetButtonClicked(val month: Month, val year: Int) : Event()
}
