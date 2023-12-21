package dev.efrenospino.exptracker.ui.screens.expense.form

import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.ui.utils.Message

data class ExpenseFormScreenState(
    val expenseId: String?,
    val expense: Expense?,
    val message: Message?,
)
