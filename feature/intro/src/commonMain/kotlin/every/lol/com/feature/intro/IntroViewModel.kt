package every.lol.com.feature.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import every.lol.com.feature.intro.model.IntroUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class IntroEvent {
    data object NavigateHome: IntroEvent()
    data class ShowErrorSnackbar(val throwable: Throwable): IntroEvent()
    data object ShowNetworkDialog: IntroEvent()
    data object ShowErrorDialog: IntroEvent()
}

class IntroViewModel(
    // private val putUserInitialUseCase: PutUserInitialUseCase,
    // private val testLoginUseCase: TestLoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(IntroUiModel())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<IntroEvent>()
    val event = _event.asSharedFlow()

    private var pendingInviteCode: String? = null
    private var launchFromOneLink: Boolean = false

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            delay(1500)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onLoginSuccess(accessToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isHaveToSignup = true,
                    token = accessToken
                )
            }
        }
    }

    fun onSignupSuccess() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false, successToSignup = true, isHaveToSignup = false) }
        }
    }

    fun testLoginUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
            _event.emit(IntroEvent.NavigateHome)
        }
    }

    fun putUserInitial() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false, isHaveToSignup = false) }
            _event.emit(IntroEvent.NavigateHome)
        }
    }

    fun deleteLoginInfo() {
        _uiState.update {
            it.copy(
                isHaveToSignup = false,
                nickName = "",
                token = "",
                isEnabled = false
            )
        }
    }
    fun checkNickname(nickName: String){

    }

    fun changeNickName(nickName: String) {
        _uiState.update {
            it.copy(nickName = nickName, isEnabled = validateNickName(nickName))
        }
    }

    private fun validateNickName(nickName: String): Boolean {
        val trimmed = nickName.trim()
        return trimmed.isNotEmpty() && trimmed.length <= 5
    }

    fun markLaunchedFromOneLink(fromOneLink: Boolean) {
        launchFromOneLink = fromOneLink
    }
}