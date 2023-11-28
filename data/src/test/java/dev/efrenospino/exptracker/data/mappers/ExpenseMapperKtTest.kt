package dev.efrenospino.exptracker.data.mappers

import dev.efrenospino.exptracker.data.models.Expense
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ExpenseMapperKtTest {

    @Test
    fun `create Expense object from expenseToAppModel`() {
        val now = ZonedDateTime.now()
        val expense = Expense(
            id = "SomeId",
            summary = "Fancy Expense",
            amount = 100.0,
            createdAt = now
        )
        assertEquals(
            expense, expenseToAppModel(
                id = "SomeId",
                summary = "Fancy Expense",
                amount = 100.0,
                createdAt = now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
            )
        )

    }

    @Test
    fun `create Expense (DB Object) from existing Expense`() {
        val now = ZonedDateTime.now()
        val expense = Expense(
            id = "SomeId",
            summary = "Fancy Expense",
            amount = 100.0,
            createdAt = now
        )
        val expenseDbObject = dev.efrenospino.data.db.tables.Expense(
            id = "SomeId",
            summary = "Fancy Expense",
            amount = 100.0,
            createdAt = now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        )
        assertEquals(expenseDbObject, expense.asDbObject())
    }
}