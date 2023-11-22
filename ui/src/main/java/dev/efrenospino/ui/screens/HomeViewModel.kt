package dev.efrenospino.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.efrenospino.exptracker.data.repositories.ExpensesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class HomeViewModel(private val expensesRepository: ExpensesRepository) : ViewModel() {

    private val viewModelState =
        MutableStateFlow(
            HomeScreenState(
                expensesList = emptyList(),
                thisMonth = ZonedDateTime.now().month,
                error = null
            )
        )
    val uiState = viewModelState.asStateFlow()

    fun loadAllExpenses(dispatcher: CoroutineDispatcher = Dispatchers.IO) =
        viewModelScope.launch(dispatcher) {
            runCatching {
                expensesRepository.getAllExpenses()
            }.onSuccess { data ->
                viewModelState.update {
                    it.copy(expensesList = data, error = null)
                }
            }.onFailure { throwable ->
                viewModelState.update {
                    it.copy(error = throwable)
                }
            }
        }
}

