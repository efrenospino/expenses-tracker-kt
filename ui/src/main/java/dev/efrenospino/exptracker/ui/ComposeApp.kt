package dev.efrenospino.exptracker.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dev.efrenospino.exptracker.domain.usecases.FilterExpensesByMonth
import dev.efrenospino.exptracker.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.repositories.ExpensesRepositoryImpl
import dev.efrenospino.exptracker.data.sources.LocalDatabase
import dev.efrenospino.exptracker.ui.nav.AppNavigator
import dev.efrenospino.exptracker.ui.nav.AppNavigatorImpl
import dev.efrenospino.exptracker.ui.nav.Destination
import dev.efrenospino.exptracker.ui.nav.NavHost
import dev.efrenospino.exptracker.ui.nav.expensesHomeRoute
import dev.efrenospino.exptracker.ui.nav.newExpenseRoute
import dev.efrenospino.exptracker.ui.nav.viewAndEditExpenseRoute
import dev.efrenospino.exptracker.ui.screens.expense.form.ExpenseFormScreen
import dev.efrenospino.exptracker.ui.screens.expense.form.ExpenseFormViewModel
import dev.efrenospino.exptracker.ui.screens.expense.home.HomeScreen
import dev.efrenospino.exptracker.ui.screens.expense.home.HomeViewModel
import dev.efrenospino.exptracker.ui.theme.ExpensesTrackerTheme

@Composable
fun ComposeApp(context: Context) {
    ExpensesTrackerTheme {
        val navController = rememberNavController()
        val appNavigator: AppNavigator = remember { AppNavigatorImpl() }
        NavHost(
            navController = navController,
            startDestination = Destination.HomeDestination
        ) {
            expensesHomeRoute {
                HomeScreen(
                    navController = navController,
                    viewModel = viewModel {
                        HomeViewModel(
                            appNavigator = appNavigator,
                            filterExpensesByMonth = FilterExpensesByMonth(
                                expensesRepository = ExpensesRepositoryImpl(
                                    LocalDatabase(context = context)
                                )
                            ),
                            sortExpensesList = SortExpensesList()
                        )
                    },
                )
            }
            newExpenseRoute {
                ExpenseFormScreen(
                    navController = navController,
                    viewModel = viewModel {
                        ExpenseFormViewModel(
                            appNavigator = appNavigator,
                            expensesRepository = ExpensesRepositoryImpl(
                                LocalDatabase(context = context)
                            ),
                            savedStateHandle = it.savedStateHandle
                        )
                    },
                    screenTitle = "New Expense",
                )
            }
            viewAndEditExpenseRoute {
                ExpenseFormScreen(
                    navController = navController,
                    viewModel = viewModel {
                        ExpenseFormViewModel(
                            appNavigator = appNavigator,
                            expensesRepository = ExpensesRepositoryImpl(
                                LocalDatabase(context = context)
                            ),
                            savedStateHandle = it.savedStateHandle
                        )
                    },
                    screenTitle = "Edit Expense",
                )
            }
        }
    }
}
