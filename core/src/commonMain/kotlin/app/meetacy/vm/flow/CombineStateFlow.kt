package app.meetacy.vm.flow

import app.meetacy.vm.extension.stub
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlin.jvm.JvmName

@Suppress("UNCHECKED_CAST")
@JvmName("combineStatesReceiver")
public inline fun <T1, T2, T3, R> StateFlow<T1>.combineStates(
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    crossinline transform: (T1, T2, T3) -> R
): StateFlow<R> = combineStates(
    flows = arrayOf(this, flow2, flow3),
    transform = { values ->
        transform(
            values[0] as T1,
            values[1] as T2,
            values[2] as T3
        )
    }
)

@Suppress("UNCHECKED_CAST")
public inline fun <T1, T2, T3, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    crossinline transform: (T1, T2, T3) -> R
): StateFlow<R> = combineStates(
    flows = arrayOf(flow1, flow2, flow3),
    transform = { values ->
        transform(
            values[0] as T1,
            values[1] as T2,
            values[2] as T3
        )
    }
)

@Suppress("UNCHECKED_CAST")
@JvmName("combineStatesReceiver")
public inline fun <T1, T2, R> StateFlow<T1>.combineStates(
    flow2: StateFlow<T2>,
    crossinline transform: (T1, T2) -> R
): StateFlow<R> = combineStates(
    flows = arrayOf(this, flow2),
    transform = { values ->
        transform(
            values[0] as T1,
            values[1] as T2
        )
    }
)

@Suppress("UNCHECKED_CAST")
public inline fun <T1, T2, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    crossinline transform: (T1, T2) -> R
): StateFlow<R> = combineStates(
    flows = arrayOf(flow1, flow2),
    transform = { values ->
        transform(
            values[0] as T1,
            values[1] as T2
        )
    }
)

public inline fun <reified T, R> combineStates(
    vararg flows: StateFlow<T>,
    crossinline transform: (Array<T>) -> R
): StateFlow<R> = object : StateFlow<R> {
    override val replayCache: List<R> get() = listOf(value)

    override val value: R get() = transform(values)

    private val values: Array<T> = Array(flows.size) { flows[it].value }
        get() {
            for ((i, flow) in flows.withIndex()) {
                field[i] = flow.value
            }
            return field
        }

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        combine(
            flows = flows,
            transform = { transform(it) }
        ).collect(collector)
        stub()
    }
}