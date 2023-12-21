package dev.efrenospino.exptracker.domain.usecases

import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.exptracker.data.repositories.ExpensesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Month

class FilterExpensesByMonth(private val expensesRepository: ExpensesRepository) :
    UseCase.Async<FilterExpensesByMonth.Args, Flow<List<Expense>>> {

    override suspend fun invoke(dispatcher: CoroutineDispatcher, args: Args): Flow<List<Expense>> {
        return expensesRepository.getAllExpenses().map { expenses ->
            expenses.filter { it.createdAt.month == args.month && it.createdAt.year == args.year }
        }
    }

    data class Args(val month: Month, val year: Int) : UseCase.Args

}
