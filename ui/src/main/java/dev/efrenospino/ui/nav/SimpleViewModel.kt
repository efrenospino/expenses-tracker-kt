package dev.efrenospino.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class SimpleViewModel<S, E>(
    protected val appNavigator: AppNavigator,
    protected val viewModelState: MutableStateFlow<S>,
) : ViewModel() {

    val uiState: StateFlow<S> = viewModelState.stateIn(
        scope = viewModelScope,
        initialValue = viewModelState.value,
        started = SharingStarted.Eagerly
    )
    val navigationChannel: Channel<NavigationIntent> = appNavigator.navigationChannel

    abstract fun onEvent(event: E, coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO)

    protected fun onNavigateBack() {
        appNavigator.tryNavigateBack()
    }
}
