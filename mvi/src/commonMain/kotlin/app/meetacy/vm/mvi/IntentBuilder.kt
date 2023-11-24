package app.meetacy.vm.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlin.jvm.JvmName

@DslMarker
public annotation class IntentBuilderDsl

public class IntentBuilder<TState, TEffect>(
    initial: TState,
    public val scope: CoroutineScope,
    private val collector: FlowCollector<Intent.Update<TState, TEffect>>,
) {
    private var _state = initial

    @IntentBuilderDsl
    public val currentState: TState get() = _state

    @IntentBuilderDsl
    public suspend fun reduce(transform: suspend TState.() -> TState) {
        _state = currentState.transform()
        collector.emit(Intent.Update.State(currentState))
    }

    @JvmName("performEffect")
    @IntentBuilderDsl
    public suspend fun perform(effect: TEffect) {
        collector.emit(Intent.Update.Effect(effect))
    }
}
