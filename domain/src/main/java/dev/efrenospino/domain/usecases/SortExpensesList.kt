package dev.efrenospino.domain.usecases

import dev.efrenospino.exptracker.data.models.Expense

class SortExpensesList : UseCase.Run<SortExpensesList.Args, List<Expense>> {

    override operator fun invoke(args: Args): List<Expense> {
        return when (args.sortBy) {
            SortBy.AMOUNT -> args.expensesList.sortedByDescending(args.orderDescending) { it.amount }
            SortBy.CREATED -> args.expensesList.sortedByDescending(args.orderDescending) { it.createdAt }
        }
    }

    private inline fun <T, R : Comparable<R>> Iterable<T>.sortedByDescending(
        descending: Boolean,
        crossinline selector: (T) -> R?,
    ): List<T> {
        return if (descending) {
            sortedByDescending(selector)
        } else {
            sortedBy(selector)
        }
    }

    data class Args(
        val expensesList: List<Expense>,
        val sortBy: SortBy,
        val orderDescending: Boolean,
    ) : UseCase.Args

    enum class SortBy {
        AMOUNT, CREATED
    }

}
