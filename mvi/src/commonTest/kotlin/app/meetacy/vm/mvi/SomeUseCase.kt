package app.meetacy.vm.mvi

import kotlinx.coroutines.flow.Flow

interface SomeUseCase {

    fun getFlow(): Flow<Int>
}