package app.meetacy.vm.mvi

import kotlinx.coroutines.flow.Flow

public interface Intent<TState, out TEffect> {

    public fun flowOf(state: TState): Flow<Update<TState, TEffect>>

    public sealed interface Update<out TState, out TEffect> {

        public data class State<TState>(public val state: TState): Update<TState, Nothing>

        public data class Effect<TEffect>(public val effect: TEffect): Update<Nothing, TEffect>
    }
}