package dev.efrenospino.ui.screens.expense.form

import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.ui.utils.Message

data class ExpenseFormScreenState(
    val expenseId: String?,
    val expense: Expense?,
    val message: Message?,
)