package app.meetacy.vm.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

public fun <T : Any> Flow<T>.cStateIn(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    initialValue: T
): CStateFlow<T> = stateIn(scope, started, initialValue).cStateFlow()

public fun <T : Any> ReceiveChannel<T>.receiveAsCFlow(): CFlow<T> = receiveAsFlow().cFlow()
