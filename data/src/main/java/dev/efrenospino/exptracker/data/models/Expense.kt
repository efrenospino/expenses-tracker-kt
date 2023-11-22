package dev.efrenospino.exptracker.data.models

import java.time.ZonedDateTime
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val summary: String,
    val amount: Double,
    val createdAt: ZonedDateTime,
)