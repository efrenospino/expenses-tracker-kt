package dev.efrenospino.exptracker.data.repositories

import dev.efrenospino.exptracker.data.models.Expense
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {
    suspend fun getAllExpenses(coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<List<Expense>>

    suspend fun getExpenseById(expenseId: String): Expense
    suspend fun save(expense: Expense)
}
