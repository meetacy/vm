package app.meetacy.vm.mvi

import app.meetacy.vm.ViewModel
import app.meetacy.vm.extension.launchIn
import app.meetacy.vm.flow.CSharedFlow
import app.meetacy.vm.flow.CStateFlow
import app.meetacy.vm.flow.cSharedFlow
import app.meetacy.vm.flow.cStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

public abstract class MviViewModel<State : Any, Action, Event>(initialState: State) : ViewModel() {

    private val _states: MutableStateFlow<State> = MutableStateFlow(initialState)

    private val _actions: MutableSharedFlow<Action?> = MutableSharedFlow(
        replay = REPLY_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    public val states: CStateFlow<State>
        get() = _states.cStateFlow()
    public val actions: CSharedFlow<Action?>
        get() = _actions.asSharedFlow().cSharedFlow()

    public abstract fun handleEvent(event: Event)

    protected var viewState: State
        get() = _states.value
        set(value) {
            _states.update { value }
        }

    protected var viewAction: Action?
        get() = _actions.replayCache.lastOrNull()
        set(value) {
            _actions.tryEmit(value)
        }

    protected fun viewModelScopeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }

    protected fun State.mutate(block: (State) -> State = { it }): Unit = _states.update { block(this) }

    protected fun <T> Flow<T>.observe(block: suspend (T) -> Unit): Job = launchIn(viewModelScope, block)

    protected fun Action.send() { viewAction = this }

    protected fun performAction(action: Action) {
        viewAction = action
    }

    private companion object {
        const val REPLY_CAPACITY = 1
    }
}
