package dev.efrenospino.ui.screens

import dev.efrenospino.exptracker.data.models.Expense
import java.time.Month

data class HomeScreenState(
    val expensesList: List<Expense>,
    val thisMonth: Month,
    val error: Throwable?,
) {
    val thisMonthSpend: Double
        get() = expensesList.sumOf { it.amount }
}