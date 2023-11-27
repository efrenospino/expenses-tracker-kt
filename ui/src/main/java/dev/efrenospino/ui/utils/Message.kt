package dev.efrenospino.ui.utils

sealed interface Message {

    fun readableError(): String

    sealed class Info : Message {
        data object ExpenseSaved : Info() {
            override fun readableError(): String {
                return "Success!"
            }
        }
    }

    sealed class Error(protected open val throwable: Throwable) : Message {
        data class ExpenseDataNotLoaded(override val throwable: Throwable) : Error(throwable) {
            override fun readableError(): String {
                return "Expense data could not be loaded \n\n${throwable.localizedMessage}"
            }
        }

        data class ExpenseNotSaved(override val throwable: Throwable) : Error(throwable) {
            override fun readableError(): String {
                return "Expense data could not be saved \n\n${throwable.message}"
            }
        }
    }

}