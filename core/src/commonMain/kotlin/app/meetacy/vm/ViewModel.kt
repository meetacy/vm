package app.meetacy.vm

import kotlinx.coroutines.CoroutineScope

public expect open class ViewModel() {

    public val viewModelScope: CoroutineScope
    public fun onCleared()
}



