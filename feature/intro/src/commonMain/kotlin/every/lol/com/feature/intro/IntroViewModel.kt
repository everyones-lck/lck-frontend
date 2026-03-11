package every.lol.com.feature.intro

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
                    val errorMessage = error.message ?: "알 수 없는 오류"
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
            val normalizedName = currentState.nickName.trim()
            if (normalizedName.isEmpty() || normalizedName.length > 5) return
            viewModelScope.launch {
                _uiState.value = currentState.copy(
                    nickName = normalizedName,
                    isLoading = true
                            )
                _uiState.value = IntroUiState.SignupComplete(nickName = normalizedName)
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