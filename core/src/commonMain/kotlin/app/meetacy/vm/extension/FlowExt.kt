package app.meetacy.vm.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

public fun <T> FlowCollector<T>.launchEmit(scope: CoroutineScope, value: T): Job = scope.launch { emit(value) }

public fun <T> Flow<T>.launchIn(scope: CoroutineScope, block: suspend (T) -> Unit): Job =
    onEach(block).launchIn(scope)

public suspend fun <T : Any> Flow<T?>.firstNotNull(): T = filterNotNull().first()

public fun <T, R> Flow<T>.chunked(size: Int, transform: (List<T>) -> R): Flow<R> {
    require(size > 0) { "Invalid chunk size. Must be more than zero, got $size" }
    return flow {
        val list = mutableListOf<T>()
        collect { value ->
            list += value
            if (list.size == size) {
                emit(transform(list))
                list.clear()
            }
        }

    }
}

public fun <T, R> Flow<T>.windowed(
    size: Int,
    includePartialParts: Boolean = false,
    transform: (List<T>) -> R
): Flow<R> {
    require(size > 0) { "Invalid window size. Must be more than zero, got $size" }
    return flow {
        val list = mutableListOf<T>()
        collect { value ->
            list += value
            if (list.size > size) {
                list.removeAt(index = 0)
                emit(transform(list))
            } else if (includePartialParts) {
                emit(transform(list))
            }
        }
        if (includePartialParts) {
            List(list.size) { index: Int -> list.drop(index + 1) }
                // the last is just empty list
                .dropLast(1)
                .forEach { emit(transform(it)) }
        }
    }
}
