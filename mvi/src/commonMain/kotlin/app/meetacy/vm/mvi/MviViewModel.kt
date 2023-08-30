package app.meetacy.vm.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import app.meetacy.vm.ViewModel
import app.meetacy.vm.flow.CSharedFlow
import app.meetacy.vm.flow.CStateFlow
import app.meetacy.vm.flow.cSharedFlow
import app.meetacy.vm.flow.cStateFlow

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

    private companion object {
        const val REPLY_CAPACITY = 1
    }
}
