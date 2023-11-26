package app.meetacy.vm.mvi

import app.meetacy.vm.ViewModel
import app.meetacy.vm.extension.launchIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

public abstract class StateHostedViewModel<TState, TEffect>: ViewModel(), StateHost<TState, TEffect> {

    protected val state: TState get() = holder.states.value

    protected fun viewModelScopeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }

    protected fun <T> Flow<T>.observe(block: suspend (T) -> Unit): Job = launchIn(viewModelScope, block)

    protected fun accept(intent: Intent<TState, TEffect>) {
        viewModelScope.launch { holder.accept(intent) }
    }

    protected fun mutateState(transform: TState.() -> TState) {
        holder.accept(holder.states.value.transform())
    }

    protected fun perform(effect: TEffect) {
        viewModelScope.launch { holder.perform(effect) }
    }
}
