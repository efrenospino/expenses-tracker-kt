package dev.efrenospino.exptracker.data.repositories

import dev.efrenospino.exptracker.data.models.Expense

interface ExpensesRepository {
    suspend fun getAllExpenses(): List<Expense>
    suspend fun save(expense: Expense)
}
