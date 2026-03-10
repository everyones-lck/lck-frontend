package every.lol.com.feature.intro

import every.lol.com.core.domain.DomainException.InvalidInputException
import every.lol.com.core.domain.DomainException.NetworkException
import every.lol.com.core.domain.DomainException.NoPermissionException
import every.lol.com.core.domain.DomainException.ServerErrorException
import every.lol.com.core.domain.DomainException.UserNotFoundException
import every.lol.com.core.domain.usecase.LoginUseCase
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
    private val loginUseCase: LoginUseCase
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
            is IntroIntent.ClickLogin -> handleUserLogin(intent.token)
            is IntroIntent.InputNickName -> handleInputNickName(intent.nickName)
            IntroIntent.ClickSignupSubmit -> handleSignupSubmit()
            is IntroIntent.ClickTosDetail -> handleTosDetailClick(intent.id)
            IntroIntent.ClickBackToSignup -> handleBackToSignup()
            IntroIntent.ClickStartApp -> handleStartApp()
        }
    }

    private fun handleUserLogin(token: String) {
        viewModelScope.launch {
            _uiState.update { IntroUiState.Loading }
            loginUseCase(token)
                .onSuccess {
                    _event.emit(IntroEvent.NavigateHome)
                }
                .onFailure { error ->
                    error.printStackTrace()
                    _uiState.update { IntroUiState.Login }
                    val errorMessage = when (error) {
                        is UserNotFoundException -> {
                            _uiState.update { IntroUiState.Signup() }
                            return@onFailure
                        }
                        is ServerErrorException -> "서버 오류: 서비스에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
                        is NetworkException -> "네트워크 연결에 문제가 발생했습니다. 인터넷 연결을 확인해주세요."
                        is InvalidInputException -> "입력값이 올바르지 않습니다. 다시 확인해주세요."
                        is NoPermissionException -> "권한이 없습니다. 관리자에게 문의해주세요."
                        else -> "알 수 없는 오류가 발생했습니다: ${error.message ?: "Unknown error"}"
                    }
                    _event.emit(IntroEvent.ShowErrorSnackbar(Throwable(errorMessage)))
                }
        }
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            delay(1500)
            _uiState.value = IntroUiState.Login
        }
    }

    private fun handleInputNickName(name: String) {
        val currentState = _uiState.value
        if (currentState is IntroUiState.Signup) {
            _uiState.value = currentState.copy(
                nickName = name,
                isEnabled = name.trim().isNotEmpty() && name.trim().length <= 5
            )
        }
    }

    private fun handleSignupSubmit() {
        val currentState = _uiState.value
        if (currentState is IntroUiState.Signup) {
            viewModelScope.launch {
                _uiState.value = currentState.copy(isLoading = true)
                _uiState.value = IntroUiState.SignupComplete(nickName = currentState.nickName)
            }
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