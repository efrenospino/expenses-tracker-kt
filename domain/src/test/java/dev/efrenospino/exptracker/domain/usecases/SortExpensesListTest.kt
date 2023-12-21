package dev.efrenospino.exptracker.domain.usecases

import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.domain.usecases.SortExpensesList
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class SortExpensesListTest {

    private val sortExpensesList = SortExpensesList()

    private val rawList = listOf(
        Expense(id = "UUID1", summary = "Expense1", amount = 11.0, createdAt = zonedDateTimeOf(10)),
        Expense(id = "UUID2", summary = "Expense2", amount = 20.0, createdAt = zonedDateTimeOf(8)),
        Expense(id = "UUID3", summary = "Expense3", amount = 96.0, createdAt = zonedDateTimeOf(16)),
        Expense(id = "UUID4", summary = "Expense4", amount = 54.0, createdAt = zonedDateTimeOf(5)),
        Expense(id = "UUID5", summary = "Expense5", amount = 12.0, createdAt = zonedDateTimeOf(25))
    )

    @Test
    fun `sorted list by creation date and ascending order`() {
        assertEquals(
            listOf(
                rawList[3],
                rawList[1],
                rawList[0],
                rawList[2],
                rawList[4],
            ), sortExpensesList(
                args = SortExpensesList.Args(
                    expensesList = rawList,
                    sortBy = SortExpensesList.SortBy.CREATED,
                    orderDescending = false
                )
            )
        )
    }

    @Test
    fun `sorted list by creation date and descending order`() {
        assertEquals(
            listOf(
                rawList[4],
                rawList[2],
                rawList[0],
                rawList[1],
                rawList[3],
            ), sortExpensesList(
                args = SortExpensesList.Args(
                    expensesList = rawList,
                    sortBy = SortExpensesList.SortBy.CREATED,
                    orderDescending = true
                )
            )
        )
    }

    @Test
    fun `sorted list by amount date and ascending order`() {
        assertEquals(
            listOf(
                rawList[0],
                rawList[4],
                rawList[1],
                rawList[3],
                rawList[2],
            ), sortExpensesList(
                args = SortExpensesList.Args(
                    expensesList = rawList,
                    sortBy = SortExpensesList.SortBy.AMOUNT,
                    orderDescending = false
                )
            )
        )
    }

    @Test
    fun `sorted list by amount date and descending order`() {
        assertEquals(
            listOf(
                rawList[2],
                rawList[3],
                rawList[1],
                rawList[4],
                rawList[0],
            ), sortExpensesList(
                args = SortExpensesList.Args(
                    expensesList = rawList,
                    sortBy = SortExpensesList.SortBy.AMOUNT,
                    orderDescending = true
                )
            )
        )
    }

    private fun zonedDateTimeOf(day: Int): ZonedDateTime {
        return ZonedDateTime.of(
            LocalDate.of(2023, 11, day), LocalTime.NOON, ZoneId.of("Z")
        )
    }
}
