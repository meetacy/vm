package app.meetacy.vm.flow

public interface Disposable : kotlinx.coroutines.DisposableHandle

public fun Disposable(block: () -> Unit): Disposable {
    return object : Disposable {
        override fun dispose() = block()
    }
}

public operator fun Disposable.plus(other: Disposable): Disposable {
    return Disposable {
        this.dispose()
        other.dispose()
    }
}
