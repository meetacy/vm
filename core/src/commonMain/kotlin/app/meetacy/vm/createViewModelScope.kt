package app.meetacy.vm

import kotlinx.coroutines.CoroutineScope
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
public var createViewModelScope: () -> CoroutineScope = {
    CoroutineScope(createCoroutineContext())
}
