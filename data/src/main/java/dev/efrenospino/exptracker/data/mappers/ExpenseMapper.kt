package dev.efrenospino.exptracker.data.mappers

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun dev.efrenospino.data.db.tables.Expense.asAppModel(): dev.efrenospino.exptracker.data.models.Expense {
    return dev.efrenospino.exptracker.data.models.Expense(
        id,
        summary,
        amount,
        ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_INSTANT)
    )
}

fun dev.efrenospino.exptracker.data.models.Expense.asTable(): dev.efrenospino.data.db.tables.Expense {
    return dev.efrenospino.data.db.tables.Expense(
        id,
        summary,
        amount,
        createdAt.format(DateTimeFormatter.ISO_INSTANT)
    )
}
