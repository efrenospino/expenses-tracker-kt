package dev.efrenospino.exptracker.data.mappers

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun expenseToAppModel(
    id: String,
    summary: String,
    amount: Double,
    createdAt: String,
): dev.efrenospino.exptracker.data.models.Expense {
    return dev.efrenospino.exptracker.data.models.Expense(
        id,
        summary,
        amount,
        ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    )
}

fun dev.efrenospino.exptracker.data.models.Expense.asDbObject(): dev.efrenospino.data.db.tables.Expense {
    return dev.efrenospino.data.db.tables.Expense(
        id,
        summary,
        amount,
        createdAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    )
}
