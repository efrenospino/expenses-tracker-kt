package dev.efrenospino.exptracker.ui.nav

sealed class Destination(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    data object HomeDestination : NoArgumentsDestination(route = ROUTE_EXPENSES_HOME)
    data object NewExpenseDestination :
        NoArgumentsDestination(route = "$ROUTE_EXPENSES_HOME/$ROUTE_NEW")

    data object EditExpenseDestination :
        Destination(
            route = ROUTE_EXPENSES_HOME,
            params = arrayOf(ARG_EXPENSE_ID)
        ) {

        operator fun invoke(expenseId: String): String {
            return route.appendParams(
                ARG_EXPENSE_ID to expenseId,
            )
        }

    }

    companion object {
        const val ROUTE_EXPENSES_HOME = "/expenses"
        const val ROUTE_NEW = "/new"

        const val ARG_EXPENSE_ID = "expenseId"
    }

}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}

