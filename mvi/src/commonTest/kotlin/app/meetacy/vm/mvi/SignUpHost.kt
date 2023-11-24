package app.meetacy.vm.mvi

import kotlinx.coroutines.CoroutineScope

object SignUpHost: IntentHost<SignUpHost.State, SignUpHost.SideEffect > {
    interface RegisterUseCase {

        suspend fun register(userName: String): Result<Unit>
    }

    sealed interface SideEffect {
        object RouteMain : SideEffect
        object ShowError : SideEffect
    }

    data class State(
        val userName: String,
        val isLoading: Boolean
    ) {
        companion object {
            val Initial = State(
                userName = "",
                isLoading = true
            )
        }
    }

    fun signUpIntent(
        text: String,
        useCase: RegisterUseCase
    ) = intent {
        reduce {
            copy(
                isLoading = true,
                userName = text
            )
        }

        useCase.register(currentState.userName).onSuccess {
            perform(SideEffect.RouteMain)
        }.onFailure {
            perform(SideEffect.ShowError)
        }

        reduce { copy(isLoading = false) }
    }
}


