package app.meetacy.vm.flow

import app.meetacy.vm.extension.stub
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

public inline fun <T, R> StateFlow<T>.delegateState(
    crossinline transform: (T) -> R
): StateFlow<R> = object : StateFlow<R> {
    val base = this@delegateState

    override val replayCache get() = listOf(value)

    override val value get() = transform(base.value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        base
            .map { transform(it) }
            .collect(collector)
        stub()
    }
}