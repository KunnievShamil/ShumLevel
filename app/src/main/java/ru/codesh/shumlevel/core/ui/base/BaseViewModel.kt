package ru.codesh.shumlevel.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E : BaseUiEvent>(initialState: S) : ViewModel() {

    protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = mutableState.asStateFlow()

    protected val mutableEvent = Channel<E>()
    val event = mutableEvent.receiveAsFlow()

    internal fun launchCoroutine(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            block.invoke()
        }
    }

    internal fun launchIOCoroutine(block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            block.invoke()
        }
    }
}