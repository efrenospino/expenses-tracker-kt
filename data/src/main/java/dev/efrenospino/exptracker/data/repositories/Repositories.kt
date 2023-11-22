package dev.efrenospino.exptracker.data.repositories

import dev.efrenospino.exptracker.data.models.Expense

interface Repositories {

    interface ExpensesRepository {
        suspend fun getAllExpenses(): List<Expense>
        suspend fun save(expense: Expense)
    }
}
