package app.meetacy.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

public class ViewModelFactory(
    private val viewModelBlock: () -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelBlock() as T
    }
}

public inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(
    noinline viewModelBlock: () -> T
): T = ViewModelProvider(
    this,
    ViewModelFactory { viewModelBlock() }
)[T::class.java]

public inline fun <reified T : ViewModel> createViewModelFactory(
    noinline viewModelBlock: () -> T
): ViewModelFactory = ViewModelFactory {
    viewModelBlock()
}
