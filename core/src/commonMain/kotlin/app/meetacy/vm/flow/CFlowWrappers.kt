@file:Suppress("FunctionName")

package app.meetacy.vm.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public class CStateFlow<out T>(private val origin: StateFlow<T>) : StateFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CSharedFlow<out T>(private val origin: SharedFlow<T>) :
    SharedFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CFlow<out T>(private val origin: Flow<T>) : Flow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CMutableStateFlow<T>(private val origin: MutableStateFlow<T>) :
    MutableStateFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CMutableSharedFlow<T>(private val origin: MutableSharedFlow<T>) :
    MutableSharedFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
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
