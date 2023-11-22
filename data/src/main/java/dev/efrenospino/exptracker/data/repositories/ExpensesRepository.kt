package dev.efrenospino.exptracker.data.repositories

import dev.efrenospino.exptracker.data.mappers.asAppModel
import dev.efrenospino.exptracker.data.mappers.asTable
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.data.sources.LocalDatabase

class ExpensesRepositoryImpl(private val localDatabase: LocalDatabase) :
    ExpensesRepository {
    override suspend fun getAllExpenses(): List<Expense> {
        return localDatabase.expenseQueries.selectAll().executeAsList().map { it.asAppModel() }
    }

    override suspend fun save(expense: Expense) {
        localDatabase.expenseQueries.insertOrReplace(expense.asTable())
    }
}
