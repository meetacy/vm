package app.meetacy.vm.mvi

import app.meetacy.vm.extension.launchIn

class SomeViewModel: StateHostedViewModel<SomeViewModel.State, SomeViewModel.Effect>() {

    override val holder: StateHolder<State, Effect> = holder(State())

    data class State(val isLoading: Boolean = true)

    sealed interface Effect

    companion object : IntentHost<State, Effect> {

        fun subscription(useCase: SomeUseCase) = intent {
            useCase.getFlow().launchIn(scope) { value ->
                reduce { copy(isLoading = value % 3 == 0) }
            }
        }
    }
}