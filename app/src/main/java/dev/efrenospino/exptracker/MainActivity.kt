package dev.efrenospino.exptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dev.efrenospino.domain.usecases.FilterExpensesByMonth
import dev.efrenospino.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.repositories.ExpensesRepositoryImpl
import dev.efrenospino.exptracker.data.sources.LocalDatabase
import dev.efrenospino.exptracker.ui.theme.ExpensesTrackerTheme
import dev.efrenospino.ui.nav.AppNavigator
import dev.efrenospino.ui.nav.AppNavigatorImpl
import dev.efrenospino.ui.nav.Destination
import dev.efrenospino.ui.nav.NavHost
import dev.efrenospino.ui.nav.expensesHomeRoute
import dev.efrenospino.ui.nav.newExpenseRoute
import dev.efrenospino.ui.nav.viewAndEditExpenseRoute
import dev.efrenospino.ui.screens.expense.form.ExpenseFormScreen
import dev.efrenospino.ui.screens.expense.form.ExpenseFormViewModel
import dev.efrenospino.ui.screens.expense.home.HomeScreen
import dev.efrenospino.ui.screens.expense.home.HomeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                                            LocalDatabase(this@MainActivity)
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
                                        LocalDatabase(this@MainActivity)
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
                                        LocalDatabase(this@MainActivity)
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
    }
}
