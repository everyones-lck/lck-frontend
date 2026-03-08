package every.lol.com.feature.intro.model

data class IntroUiModel(
    val isLoading: Boolean = true,
    val isHaveToSignup: Boolean = false,
    val successToSignup: Boolean = false,
    val onNavigateToTermDetail: Boolean = false,
    val termId: Int = 0,
    val isEnabled: Boolean = false,
    val nickName: String = "",
    val token: String = "",
    val loginSuccess: Boolean = false
)