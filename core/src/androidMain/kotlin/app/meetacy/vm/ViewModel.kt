package app.meetacy.vm

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.cancel

public actual open class ViewModel : ViewModel() {

    public actual val viewModelScope: CoroutineScope = createViewModelScope()

    public actual override fun onCleared() : Unit = super.onCleared().also {
        viewModelScope.cancel()
    }
}
