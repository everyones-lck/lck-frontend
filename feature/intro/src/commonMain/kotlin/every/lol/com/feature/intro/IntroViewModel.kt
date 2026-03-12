package every.lol.com.feature.intro

import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.domain.usecase.RefreshUseCase
import every.lol.com.core.domain.usecase.SignupUseCase
import every.lol.com.core.domain.usecase.SocialLoginUseCase
import every.lol.com.core.model.DomainException
import every.lol.com.core.model.Signup
import every.lol.com.core.model.Team
import every.lol.com.feature.intro.model.IntroIntent
import every.lol.com.feature.intro.model.IntroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


sealed class IntroEvent {
    data object NavigateHome : IntroEvent()
    data class ShowErrorSnackbar(val throwable: Throwable) : IntroEvent()
}

class IntroViewModel(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val nicknameUseCase: NicknameUseCase,
    private val refreshUseCase: RefreshUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<IntroUiState>(IntroUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<IntroEvent>()
    val event = _event.asSharedFlow()

    init {
        onIntent(IntroIntent.LoadInitial)
    }

    fun onIntent(intent: IntroIntent) {
        when (intent) {
            IntroIntent.LoadInitial -> checkInitialState()
            is IntroIntent.ClickLogin -> startKakaoLogin()
            is IntroIntent.InputNickName -> handleInputNickName(intent.nickName)
            is IntroIntent.ClickCheckDuplicateNickname -> checkNicknameDuplicate(intent.nickName)
            IntroIntent.ClickSignupSubmit -> handleSignupSubmit()
            is IntroIntent.ClickTosDetail -> handleTosDetailClick(intent.id)
            IntroIntent.ClickBackToSignup -> handleBackToSignup()
            IntroIntent.ClickStartApp -> handleStartApp()
            is IntroIntent.ChangeSelectedTeams -> onTeamsChanged(intent.teams)
            is IntroIntent.ChangeProfileImage -> onProfileImageChanged(intent.image)
        }
    }


    private fun checkInitialState() {
        viewModelScope.launch {
            delay(1500)
            _uiState.value = IntroUiState.Login
        }
    }

    private fun startKakaoLogin() {
        viewModelScope.launch {
            _uiState.update { IntroUiState.Loading }

            socialLoginUseCase()
                .onSuccess {
                    _event.emit(IntroEvent.NavigateHome)
                }
                .onFailure { error ->
                    if (error is DomainException.UserNotRegisteredException) {
                        _uiState.update {
                            IntroUiState.Signup(
                                kakaoUserId = error.kakaoUserId,
                                nickName = "",
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update { IntroUiState.Login }
                        _event.emit(IntroEvent.ShowErrorSnackbar(error))
                    }
                }
        }
    }

    private fun handleInputNickName(name: String) {
        val currentState = _uiState.value
        if (currentState is IntroUiState.Signup) {
            _uiState.value = currentState.copy(
                nickName = name,
                isDuplicateChecked = false,
            )
        }
    }


    private fun checkNicknameDuplicate(name: String) {
        val currentState = _uiState.value as? IntroUiState.Signup ?: return
        if (name.isBlank()) return

        viewModelScope.launch {
            nicknameUseCase(name)
                .onSuccess {
                    _uiState.value = currentState.copy(isDuplicateChecked = true)
                }
                .onFailure { error ->
                    _event.emit(IntroEvent.ShowErrorSnackbar(error))
                }
        }
    }

    private fun handleSignupSubmit() {
        val state = _uiState.value as? IntroUiState.Signup ?: return
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)

            val param = Signup(
                kakaoUserId = state.kakaoUserId,
                nickname = state.nickName,
                profileImage = state.profileImage,
                teamId = state.teamId.map { it.id }
            )

            signupUseCase(param).fold(
                onSuccess = {
                    _uiState.value = IntroUiState.SignupComplete(nickName = state.nickName)
                },
                onFailure = { error ->
                    _uiState.value = state.copy(isLoading = false)
                    _event.emit(IntroEvent.ShowErrorSnackbar(error))
                }
            )
        }
    }

    fun onTeamsChanged(teams: Set<Team>) {
        val state = _uiState.value
        if (state is IntroUiState.Signup) {
            _uiState.value = state.copy(teamId = teams)
        }
    }

    fun onProfileImageChanged(image: Any?) {
        val state = _uiState.value
        if (state is IntroUiState.Signup) {
            _uiState.value = state.copy(profileImage = image)
        }
    }

    private fun handleBackToSignup() {
        _uiState.value = IntroUiState.Signup()
    }

    private fun handleTosDetailClick(id: Int) {
        _uiState.update { IntroUiState.TosDetail(id) }
    }
    private fun handleStartApp() {
        viewModelScope.launch {
            _event.emit(IntroEvent.NavigateHome)
        }
    }
}