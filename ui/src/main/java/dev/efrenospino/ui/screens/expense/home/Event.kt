package dev.efrenospino.ui.screens.expense.home

import dev.efrenospino.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.models.Expense

sealed class Event {
    data object OnLoadAllExpenses : Event()
    data class OnSortByOptionChanged(val sortBy: SortExpensesList.SortBy) : Event()

    data class OnOrderByOptionChanged(val orderDescending: Boolean) : Event()

    data object OnRegisterNewExpenseButtonClicked : Event()
    data class OnExpenseRecordClicked(val expense: Expense) : Event()
    data object OnGoToPreviousMonthButtonClicked : Event()
    data object OnGoToNextMonthButtonClicked : Event()
    data object OnGoToCurrentMonthButtonClicked : Event()
}