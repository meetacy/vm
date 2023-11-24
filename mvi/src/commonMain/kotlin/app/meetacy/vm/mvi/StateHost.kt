package app.meetacy.vm.mvi

import app.meetacy.vm.flow.CFlow
import app.meetacy.vm.flow.CStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

public interface StateHost<TState, TEffect> {

    public val holder: StateHolder<TState, TEffect>
}

public fun <TState, TEffect> StateHost<TState, TEffect>.holder(
    initial: TState
): StateHolder<TState, TEffect> = object : StateHolder<TState, TEffect>() {
    private val _effects: Channel<TEffect> = Channel(Channel.BUFFERED)
    private val _states: MutableStateFlow<TState> = MutableStateFlow(initial)

    override val effects: CFlow<TEffect> = CFlow(_effects.receiveAsFlow())
    override val states: CStateFlow<TState> = CStateFlow(_states.asStateFlow())

    private val collector: FlowCollector<Intent.Update<TState, TEffect>> = FlowCollector { value ->
        when (value) {
            is Intent.Update.State -> _states.emit(value.state)
            is Intent.Update.Effect -> _effects.send(value.effect)
        }
    }

    override suspend fun accept(effect: TEffect) {
        _effects.send(effect)
    }

    override fun accept(newState: TState) {
        _states.update { newState }
    }

    override suspend fun accept(intent: Intent<TState, TEffect>) {
        intent.flowOf(states.value).collect(collector)
    }
}