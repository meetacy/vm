package app.meetacy.vm.mvi

import app.meetacy.vm.flow.CFlow
import app.meetacy.vm.flow.CStateFlow

public abstract class StateHolder<TState, TEffect> {

    public abstract val effects: CFlow<TEffect>
    public abstract val states: CStateFlow<TState>

    internal abstract suspend fun accept(intent: Intent<TState, TEffect>)
    internal abstract suspend fun perform(effect: TEffect)
    internal abstract fun accept(newState: TState)
}