package app.meetacy.vm.mvi

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StateHostTest {

    private fun intent(text: String) = SignUpHost.signUpIntent(
        text = text,
        useCase = object : SignUpHost.RegisterUseCase {
            override suspend fun register(userName: String): Result<Unit> = runCatching {
                if (userName == "userName2") throw IllegalStateException("Some error")
            }
        }
    )

    @Test
    fun testSignUpIntentWithoutThrows() = runTest {
        val updates = intent(text = "userName").flowOf(SignUpHost.State.Initial).toList()
        assertEquals(
            expected = listOf(
                Intent.Update.State(SignUpHost.State(isLoading = true, userName = "userName")),
                Intent.Update.Effect(SignUpHost.SideEffect.RouteMain),
                Intent.Update.State(SignUpHost.State(isLoading = false, userName = "userName"))
            ),
            updates
        )
    }

    @Test
    fun testSignUpIntentWithThrows() = runTest {
        val updates = intent(text = "userName2").flowOf(SignUpHost.State.Initial).toList()
        assertEquals(
            expected = listOf(
                Intent.Update.State(SignUpHost.State(isLoading = true, userName = "userName2")),
                Intent.Update.Effect(SignUpHost.SideEffect.ShowError),
                Intent.Update.State(SignUpHost.State(isLoading = false, userName = "userName2"))
            ),
            updates
        )
    }
}
