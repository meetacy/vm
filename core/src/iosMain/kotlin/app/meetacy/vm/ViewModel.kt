package app.meetacy.vm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

public actual open class ViewModel actual constructor() {
    public actual val viewModelScope: CoroutineScope = createViewModelScope()

    public actual open fun onCleared(): Unit = viewModelScope.cancel()
}
