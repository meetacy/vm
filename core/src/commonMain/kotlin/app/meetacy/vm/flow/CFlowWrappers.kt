@file:Suppress("FunctionName")

package app.meetacy.vm.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

public open class CFlow<out T>(private val origin: Flow<T>) : Flow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public open class CStateFlow<out T>(private val origin: StateFlow<T>) : CFlow<T>(origin), StateFlow<T> by origin {
    override val replayCache: List<T> get() = origin.replayCache

    override val value: T get() = origin.value

    override suspend fun collect(collector: FlowCollector<T>): Nothing = origin.collect(collector)
}

public open class CSharedFlow<out T>(private val origin: SharedFlow<T>) :
    CFlow<T>(origin), SharedFlow<T> by origin {
    override suspend fun collect(collector: FlowCollector<T>): Nothing = origin.collect(collector)
}

public class CMutableStateFlow<T>(private val origin: MutableStateFlow<T>) :
    CStateFlow<T>(origin), MutableStateFlow<T> by origin {

    override val replayCache: List<T> get() = origin.replayCache

    override var value: T
        get() = super.value
        set(value) {
            origin.value = value
        }

    override val subscriptionCount: StateFlow<Int> = origin.subscriptionCount

    override suspend fun emit(value: T): Unit = origin.emit(value)

    @ExperimentalCoroutinesApi
    override fun resetReplayCache(): Unit = origin.resetReplayCache()

    override fun tryEmit(value: T): Boolean = origin.tryEmit(value)

    override fun compareAndSet(expect: T, update: T): Boolean = origin.compareAndSet(expect, update)
    override suspend fun collect(collector: FlowCollector<T>): Nothing = origin.collect(collector)

}

public class CMutableSharedFlow<T>(private val origin: MutableSharedFlow<T>) :
    CSharedFlow<T>(origin), MutableSharedFlow<T> by origin {

    override val replayCache: List<T> get() = origin.replayCache
    override suspend fun collect(collector: FlowCollector<T>): Nothing = origin.collect(collector)
}

public fun <T> Flow<T>.cFlow(): CFlow<T> = CFlow(this)

public fun <T> StateFlow<T>.cStateFlow(): CStateFlow<T> = CStateFlow(this)

public fun <T> SharedFlow<T>.cSharedFlow(): CSharedFlow<T> = CSharedFlow(this)

public fun <T> MutableStateFlow<T>.cMutableStateFlow(): CMutableStateFlow<T> =
    CMutableStateFlow(this)

public fun <T> MutableSharedFlow<T>.cMutableSharedFlow(): CMutableSharedFlow<T> =
    CMutableSharedFlow(this)


public fun <T> CMutableStateFlow(value: T): CMutableStateFlow<T> =
    MutableStateFlow(value).cMutableStateFlow()

public fun <T> CMutableSharedFlow(
    replay: Int = 0,
    extraBufferCapacity: Int = 0,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
): CMutableSharedFlow<T> = MutableSharedFlow<T>(
    replay = replay,
    extraBufferCapacity = extraBufferCapacity,
    onBufferOverflow = onBufferOverflow
).cMutableSharedFlow()

private fun <T> Flow<T>.flowSubscribe(block: (T) -> Unit): Disposable =
    CoroutineScope(SupervisorJob() + Dispatchers.Main)
        .also { onEach(block).launchIn(it) }
        .let {
            Disposable {
                it.cancel()
            }
        }
