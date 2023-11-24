package app.meetacy.vm.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

public interface IntentHost<TState, TEffect>

@IntentBuilderDsl
public inline fun <TState, TEffect> IntentHost<TState, TEffect>.intent(
    crossinline builder: suspend IntentBuilder<TState, TEffect>.() -> Unit
): Intent<TState, TEffect> = buildIntent(builder)

public inline fun <TState, TEffect> buildIntent(
    crossinline builder: suspend IntentBuilder<TState, TEffect>.() -> Unit
): Intent<TState, TEffect> = object : Intent<TState, TEffect> {
    override fun flowOf(state: TState): Flow<Intent.Update<TState, TEffect>> = channelFlow {
        val intent = IntentBuilder(
            state,
            scope = this,
            collector = { this.send(it) }
        )
        intent.run { builder() }
    }
}
