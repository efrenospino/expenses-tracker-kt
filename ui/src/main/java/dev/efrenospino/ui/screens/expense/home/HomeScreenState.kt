package dev.efrenospino.ui.screens.expense.home

import dev.efrenospino.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.models.Expense
import java.time.Month
import java.time.ZonedDateTime

data class HomeScreenState(
    val expensesList: List<Expense> = emptyList(),
    val sortBy: SortExpensesList.SortBy = SortExpensesList.SortBy.CREATED,
    val orderByDescending: Boolean = true,
    val currentYear: Int = ZonedDateTime.now().year,
    val currentMonth: Month = ZonedDateTime.now().month,
    val selectedYear: Int = currentYear,
    val selectedMonth: Month = currentMonth,
    val error: Throwable? = null,
) {
    val selectedMonthSpend: Double
        get() = expensesList.sumOf { it.amount }

    val displayGoToNextMonthButton: Boolean
        get() {
            val currentDate = ZonedDateTime.now()
            return selectedYear < currentDate.year || selectedMonth.value < currentDate.month.value
        }

    val displayGoToCurrentMonth: Boolean
        get() = selectedMonth != currentMonth || selectedYear != currentYear

}
