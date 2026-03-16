package every.lol.com.feature.mypage

import every.lol.com.core.common.toImageByteArray
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.model.Team
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


sealed class MypageEvent {
    data object NavigateHome : MypageEvent()
    data object NavigateProfileEdit: MypageEvent()
    data object NavigateToMyPosts : MypageEvent()
    data object NavigateToPogVote : MypageEvent()
    data object NavigateToPrediction : MypageEvent()
    data object NavigateWithdrawal: MypageEvent()
    data object NavigateToAppInfo : MypageEvent()
    data object NavigateTos1: MypageEvent()
    data object NavigateTos2: MypageEvent()
    //data object NavigateOpenSourceLicense: MypageEvent()
    data class ShowErrorSnackbar(val throwable: Throwable) : MypageEvent()
}

class MypageViewModel(
    private val nicknameUseCase: NicknameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MypageUiState>(MypageUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MypageEvent>()
    val event = _event.asSharedFlow()

    init {
        onIntent(MypageIntent.LoadInitial)
    }

    fun onIntent(intent: MypageIntent) {
        when (intent) {
            MypageIntent.LoadMypage -> checkMypageInitialState()
            MypageIntent.LoadAppInform -> checkAppInformInitialState()
            MypageIntent.ClickBackToHome -> handleBackToHome()
            is MypageIntent.ClickMenu -> handleMenuClick(intent.type)
            MypageIntent.FetchMyPosts -> {}
            MypageIntent.FetchMyComments -> {}
            else -> {}
        }
    }

    private fun handleMenuClick(type: MypageUiState.MypageMenuType) {
        viewModelScope.launch {
            when (type) {
                MypageUiState.MypageMenuType.PROFILE_EDIT -> {
                    _event.emit(MypageEvent.NavigateProfileEdit)
                }
                MypageUiState.MypageMenuType.POST_COMMENT -> {
                    _event.emit(MypageEvent.NavigateToMyPosts)
                }
                MypageUiState.MypageMenuType.POG_VOTE -> {
                    _event.emit(MypageEvent.NavigateToPogVote)
                }
                MypageUiState.MypageMenuType.PREDICTION -> {
                    _event.emit(MypageEvent.NavigateToPrediction)
                }
                MypageUiState.MypageMenuType.LOGOUT -> {
                    handleLogout()
                }
                MypageUiState.MypageMenuType.WITHDRAWAL -> {

                }
                MypageUiState.MypageMenuType.APP_INFO -> {
                    _event.emit(MypageEvent.NavigateToAppInfo)
                }
                MypageUiState.MypageMenuType.TOS_1 -> {
                    _event.emit(MypageEvent.NavigateTos1)
                }
                MypageUiState.MypageMenuType.TOS_2 -> {
                    _event.emit(MypageEvent.NavigateTos2)
                }
                MypageUiState.MypageMenuType.OPEN_SOURCE_LICENCE -> {
                    //Todo: 오픈 소스 라이선스 화면
                }
                MypageUiState.MypageMenuType.APP_VERSION -> {
                    //아무런 작동 안함
                }
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            try {
                //todo: 로그아웃 연결, 캐시삭제 필수!!
                _event.emit(MypageEvent.NavigateHome)
            } catch (e: Exception) {
                _event.emit(MypageEvent.ShowErrorSnackbar(e))
            }
        }
    }

    private fun checkMypageInitialState() {
        viewModelScope.launch {
            val myInform = MypageUiState.MyInform(
            nickName = "김승혁",
            teamId = setOf(Team.T1)
            )

            val menuList = listOf(
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.PROFILE_EDIT, "프로필 수정"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.POST_COMMENT, "My Post / Comment"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.POG_VOTE, "POG 투표 내역"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.PREDICTION, "승부예측 투표 내역"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.LOGOUT, "로그아웃"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.WITHDRAWAL, "계정 탈퇴"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.APP_INFO, "앱 정보", showDivider = false)
            )

            _uiState.value = MypageUiState.Mypage(
                myInform = myInform,
                menuList = menuList
            )
        }
    }

    private fun checkAppInformInitialState() {
        val appInfoMenus = listOf(
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_1, "서비스 이용약관"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_2, "개인정보 처리방침"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.OPEN_SOURCE_LICENCE, "오픈소스 라이선스"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.APP_VERSION, "버전 정보", showDivider = false)
        )
        _uiState.value = MypageUiState.AppInform(menuList = appInfoMenus)
    }
    private fun handleInputNickName(name: String) {
        val currentState = _uiState.value
        if (currentState is MypageUiState.ProfileEdit) {
            _uiState.value = currentState.copy(
                nickName = name,
                isDuplicateChecked = false,
            )
        }
    }


    private fun checkNicknameDuplicate(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            nicknameUseCase(name)
                .onSuccess {
                    _uiState.update { state ->
                        if (state is MypageUiState.ProfileEdit) {
                            state.copy(isDuplicateChecked = true)
                        } else state
                    }
                }.onFailure { error ->
                    _event.emit(MypageEvent.ShowErrorSnackbar(error))
                }
        }
    }

    fun onTeamsChanged(teams: Set<Team>) {
        val state = _uiState.value
        if (state is MypageUiState.ProfileEdit) {
            _uiState.value = state.copy(teamId = teams)
        }
    }

    fun onProfileImageChanged(image: Any?) {
        val state = _uiState.value
        if (state is MypageUiState.ProfileEdit) {
            _uiState.update { state ->
                if (state is MypageUiState.ProfileEdit) {
                    state.copy(profileImage = image.toImageByteArray())
                } else state
            }
        }
    }

    private fun handleBackToHome() {
        viewModelScope.launch {
            _event.emit(MypageEvent.NavigateHome)
        }
    }

    private fun handleTosDetailClick(id: Int) {
        _uiState.update { MypageUiState.TosDetail(id) }
    }
}