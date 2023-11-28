package dev.efrenospino.exptracker.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.efrenospino.exptracker.data.mappers.asDbObject
import dev.efrenospino.exptracker.data.mappers.expenseToAppModel
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.data.sources.LocalDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class ExpensesRepositoryImpl(private val db: LocalDatabase) : ExpensesRepository {
    override suspend fun getAllExpenses(coroutineDispatcher: CoroutineDispatcher): Flow<List<Expense>> {
        return db.expenseQueries.selectAll(mapper = ::expenseToAppModel)
            .asFlow()
            .mapToList(coroutineDispatcher)
    }

    override suspend fun getExpenseById(expenseId: String): Expense {
        return db.expenseQueries.selectById(expenseId, ::expenseToAppModel).executeAsOne()
    }

    override suspend fun save(expense: Expense) {
        db.expenseQueries.insertOrReplace(expense.asDbObject())
    }
}
