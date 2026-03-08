package every.lol.com.feature.intro.model

import androidx.compose.runtime.Immutable

/*data class IntroUiModel(
    val isLoading: Boolean = true,
    val isHaveToSignup: Boolean = false,
    val successToSignup: Boolean = false,
    val onNavigateToTermDetail: Boolean = false,
    val termId: Int = 0,
    val isEnabled: Boolean = false,
    val nickName: String = "",
    val token: String = "",
    val loginSuccess: Boolean = false
)*/

@Immutable
data class IntroUiState(
    val step: IntroStep = IntroStep.Loading,
    val isLoading: Boolean = false,
    val nickName: String = "",
    val token: String = "",
    val isEnabled: Boolean = false,
    val error: Throwable? = null
)

sealed interface IntroStep {
    data object Loading : IntroStep
    data object Login : IntroStep
    data object Signup : IntroStep
    data object SignupComplete : IntroStep
    data class TosDetail(val id: Int) : IntroStep
}