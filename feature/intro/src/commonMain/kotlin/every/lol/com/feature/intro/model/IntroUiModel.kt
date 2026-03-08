package every.lol.com.feature.intro.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface IntroUiState {
    data object Loading : IntroUiState
    data object Login : IntroUiState
    data class Signup(
        val nickName: String = "",
        val isEnabled: Boolean = false,
        val isLoading: Boolean = false
    ) : IntroUiState
    data class SignupComplete(val nickName: String) : IntroUiState
    data class TosDetail(val id: Int) : IntroUiState
}

sealed interface IntroIntent {
    data object LoadInitial : IntroIntent
    data class ClickLogin(val token: String) : IntroIntent
    data class InputNickName(val nickName: String) : IntroIntent
    data object ClickSignupSubmit : IntroIntent
    data class ClickTosDetail(val id: Int) : IntroIntent
    data object ClickBackToSignup : IntroIntent
    data object ClickStartApp : IntroIntent
}