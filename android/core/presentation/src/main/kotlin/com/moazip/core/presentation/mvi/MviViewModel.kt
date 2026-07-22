package com.moazip.core.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<Intent : UiIntent, State : UiState, Effect : UiEffect>(
    initialState: State,
) : ViewModel() {
    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effect = effectChannel.receiveAsFlow()

    abstract fun onIntent(intent: Intent)

    protected fun reduce(transform: State.() -> State) {
        mutableState.update(transform)
    }

    protected fun postEffect(effect: Effect) {
        viewModelScope.launch { effectChannel.send(effect) }
    }
}
