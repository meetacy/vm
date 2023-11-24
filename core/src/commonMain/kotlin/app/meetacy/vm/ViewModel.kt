package app.meetacy.vm

import app.meetacy.vm.extension.launchIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

public expect open class ViewModel() {

    public val viewModelScope: CoroutineScope
    public fun onCleared()
}
