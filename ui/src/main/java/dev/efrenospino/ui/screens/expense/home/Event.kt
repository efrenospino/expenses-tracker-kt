package dev.efrenospino.ui.screens.expense.home

import dev.efrenospino.exptracker.data.models.Expense

sealed class Event  {
    data object OnLoadAllExpenses : Event()
    data object OnRegisterNewExpenseButtonClicked : Event()
    data class OnExpenseRecordClicked(val expense: Expense) : Event()
}
