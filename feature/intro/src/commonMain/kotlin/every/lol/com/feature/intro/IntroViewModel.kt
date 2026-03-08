package every.lol.com.feature.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import every.lol.com.feature.intro.model.IntroIntent
import every.lol.com.feature.intro.model.IntroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class IntroEvent {
    data object NavigateHome : IntroEvent()
    data class ShowErrorSnackbar(val throwable: Throwable) : IntroEvent()
}

class IntroViewModel : ViewModel() {

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
            is IntroIntent.ClickLogin -> handleLogin(intent.token)
            is IntroIntent.InputNickName -> handleInputNickName(intent.nickName)
            IntroIntent.ClickSignupSubmit -> handleSignupSubmit()
            is IntroIntent.ClickTosDetail -> _uiState.value = IntroUiState.TosDetail(intent.id)
            IntroIntent.ClickBackToSignup -> handleBackToSignup()
            IntroIntent.ClickStartApp -> handleStartApp()
        }
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            delay(1500)
            _uiState.value = IntroUiState.Login
        }
    }

    private fun handleLogin(token: String) {
        viewModelScope.launch {
            delay(1000)
            _uiState.value = IntroUiState.Signup(isLoading = false)
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
                delay(1000)
                _uiState.value = IntroUiState.SignupComplete(nickName = currentState.nickName)
            }
        }
    }

    private fun handleBackToSignup() {
        _uiState.value = IntroUiState.Signup()
    }

    private fun handleStartApp() {
        viewModelScope.launch {
            _event.emit(IntroEvent.NavigateHome)
        }
    }
}