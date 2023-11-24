package app.meetacy.vm.mvi

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SomeViewModelTest {

    @Test
    @OptIn(ExperimentalStdlibApi::class)
    fun test() = runTest {
        val useCase: SomeUseCase = object : SomeUseCase {
            override fun getFlow(): Flow<Int> = flow {
                for (element in 0..<3) {
                    emit(element)
                    delay(1000L)
                }
            }
        }

        val intent = SomeViewModel.subscription(useCase)

        assertEquals(
            expected = listOf(
                Intent.Update.State(SomeViewModel.State(true)),
                Intent.Update.State(SomeViewModel.State(false)),
                Intent.Update.State(SomeViewModel.State(false)),
            ),
            actual = intent.flowOf(SomeViewModel.State()).toList()
        )
    }
}