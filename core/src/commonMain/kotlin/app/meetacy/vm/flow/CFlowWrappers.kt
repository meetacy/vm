package app.meetacy.vm.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.jvm.JvmName

public class CStateFlow<out T : Any>(private val origin: StateFlow<T>) : StateFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CSharedFlow<out T : Any?>(private val origin: SharedFlow<T>) : SharedFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CFlow<out T : Any>(private val origin: Flow<T>) : Flow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CMutableStateFlow<T : Any>(private val origin: MutableStateFlow<T>) :
    MutableStateFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public class CMutableSharedFlow<T : Any>(private val origin: MutableSharedFlow<T>) :
    MutableSharedFlow<T> by origin {
    public fun subscribe(block: (T) -> Unit): Disposable = flowSubscribe(block)
}

public fun <T : Any> Flow<T>.cFlow(): CFlow<T> = CFlow(this)

public fun <T : Any> StateFlow<T>.cStateFlow(): CStateFlow<T> = CStateFlow(this)

public fun <T : Any> SharedFlow<T>.cSharedFlow(): CSharedFlow<T> = CSharedFlow(this)

@JvmName("cSharedFlowOptional")
public fun <T : Any?> SharedFlow<T>.cSharedFlow(): CSharedFlow<T?> = CSharedFlow(this)

public fun <T : Any> MutableStateFlow<T>.cMutableStateFlow(): CMutableStateFlow<T> =
    CMutableStateFlow(this)

public fun <T : Any> CMutableSharedFlow<T>.cMutableSharedFlow(): CMutableSharedFlow<T> =
    CMutableSharedFlow(this)

private fun <T> Flow<T>.flowSubscribe(block: (T) -> Unit): Disposable =
    CoroutineScope(SupervisorJob() + Dispatchers.Main)
        .also { onEach(block).launchIn(it) }
        .let {
            Disposable {
                it.cancel()
            }
        }
