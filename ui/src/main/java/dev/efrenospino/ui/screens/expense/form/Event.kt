package dev.efrenospino.ui.screens.expense.form

import java.time.ZonedDateTime

sealed class Event {
    data object OnNavigateBack : Event()
    data object OnResetErrors : Event()
    data object OnLoadExpenseData : Event()
    data class OnSaveButtonClicked(
        val expenseAmount: String,
        val expenseSummary: String,
        val expenseDate: ZonedDateTime,
    ) : Event()
}


