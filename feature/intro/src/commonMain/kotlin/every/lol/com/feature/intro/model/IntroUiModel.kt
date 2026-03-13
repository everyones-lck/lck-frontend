package every.lol.com.feature.intro.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.Team

@Immutable
sealed interface IntroUiState {
    data object Loading : IntroUiState
    data object Login : IntroUiState
    data class Signup(
        val profileImage: ByteArray? = null,
        val kakaoUserId: String = "",
        val nickName: String = "",
        val teamId: Set<Team> = emptySet(),
        val isDuplicateChecked: Boolean = false,
        val isEnabled: Boolean = false,
        val isLoading: Boolean = false
    ) : IntroUiState
    data class SignupComplete(val nickName: String) : IntroUiState
    data class TosDetail(val id: Int) : IntroUiState
}

sealed interface IntroIntent {
    data class InputNickName(val nickName: String) : IntroIntent
    data class ClickCheckDuplicateNickname(val nickName: String) : IntroIntent
    data class ChangeSelectedTeams(val teams: Set<Team>) : IntroIntent
    data class ChangeProfileImage(val image: Any?) : IntroIntent
    data object LoadInitial : IntroIntent
    data class ClickLogin(val token: String) : IntroIntent
    data object ClickSignupSubmit : IntroIntent
    data class ClickTosDetail(val id: Int) : IntroIntent
    data object ClickBackToSignup : IntroIntent
    data object ClickStartApp : IntroIntent
}