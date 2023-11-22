package dev.efrenospino.exptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.efrenospino.exptracker.data.repositories.ExpensesRepositoryImpl
import dev.efrenospino.exptracker.data.sources.LocalDatabase
import dev.efrenospino.exptracker.ui.theme.ExpensesTrackerTheme
import dev.efrenospino.ui.screens.HomeScreen
import dev.efrenospino.ui.screens.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpensesTrackerTheme {
                HomeScreen(
                    viewModel = HomeViewModel(
                        expensesRepository = ExpensesRepositoryImpl(
                            LocalDatabase(this)
                        )
                    )
                )
            }
        }
    }
}
