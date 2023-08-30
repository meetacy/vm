package app.meetacy.vm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

public fun createCoroutineContext(): CoroutineContext = (SupervisorJob() + Dispatchers.Main.immediate)
