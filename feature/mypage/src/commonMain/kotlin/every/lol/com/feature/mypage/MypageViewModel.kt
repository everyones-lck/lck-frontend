package every.lol.com.feature.mypage

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

    private var cachedMyInform: MypageUiState.MyInform? = null

    init {
        onIntent(MypageIntent.LoadInitial)
    }
    fun onIntent(intent: MypageIntent) {
        when (intent) {
            MypageIntent.LoadMypage -> loadMypageData()
            MypageIntent.LoadAppInform -> loadAppInformData()
            is MypageIntent.ClickMenu -> handleMenuClick(intent.type)
            MypageIntent.ClickBackToHome -> handleBackToHome()
            MypageIntent.FetchMyPosts -> loadCommunityData(MypageUiState.CommunityTab.POST)
            MypageIntent.FetchMyComments -> loadCommunityData(MypageUiState.CommunityTab.COMMENT)
            else -> {}
        }
    }

    private fun handleMenuClick(type: MypageUiState.MypageMenuType) {
        viewModelScope.launch {
            when (type) {
                MypageUiState.MypageMenuType.APP_INFO -> loadAppInformData()
                MypageUiState.MypageMenuType.PROFILE_EDIT -> loadProfileEditData()
                MypageUiState.MypageMenuType.POST_COMMENT -> loadCommunityData()
                MypageUiState.MypageMenuType.POG_VOTE -> loadMVPData()
                MypageUiState.MypageMenuType.PREDICTION -> loadPredictionData()
                MypageUiState.MypageMenuType.TOS_1 -> handleTosDetailClick(1)
                MypageUiState.MypageMenuType.TOS_2 -> handleTosDetailClick(2)
                MypageUiState.MypageMenuType.WITHDRAWAL -> _uiState.value = MypageUiState.Withdrawal

                MypageUiState.MypageMenuType.LOGOUT -> handleLogout()

                MypageUiState.MypageMenuType.APP_VERSION -> { /* 토스트 */ }
                MypageUiState.MypageMenuType.OPEN_SOURCE_LICENCE -> { /* 로직 */ }
            }
        }
    }
    private fun loadMypageData() {
        viewModelScope.launch {
            val myInform = cachedMyInform ?: MypageUiState.MyInform(
                nickName = "김승혁",
                teamId = setOf(Team.T1)
            )
            cachedMyInform = myInform

            val menuList = listOf(
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.PROFILE_EDIT, "프로필 수정"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.POST_COMMENT, "My Post / Comment"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.POG_VOTE, "POG 투표 내역"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.PREDICTION, "승부예측 투표 내역"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.LOGOUT, "로그아웃"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.WITHDRAWAL, "계정 탈퇴"),
                MypageUiState.MypageMenu(MypageUiState.MypageMenuType.APP_INFO, "앱 정보", showDivider = false)
            )

            _uiState.value = MypageUiState.Mypage(myInform = myInform, menuList = menuList)
        }
    }

    private fun loadAppInformData() {
        val appInfoMenus = listOf(
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_1, "서비스 이용약관"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_2, "개인정보 처리방침"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.OPEN_SOURCE_LICENCE, "오픈소스 라이선스"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.APP_VERSION, "버전 정보", showDivider = false)
        )
        _uiState.value = MypageUiState.AppInform(menuList = appInfoMenus)
    }

    private fun loadProfileEditData() {
        val currentMypage = _uiState.value as? MypageUiState.Mypage
        _uiState.value = MypageUiState.ProfileEdit(
            nickName = currentMypage?.myInform?.nickName ?: "",
            teamId = currentMypage?.myInform?.teamId ?: emptySet(),
            profileImage = currentMypage?.myInform?.profileImage
        )
    }

    private fun loadCommunityData(tab: MypageUiState.CommunityTab = MypageUiState.CommunityTab.POST) {
        _uiState.value = MypageUiState.Community(isLoading = true, selectedTab = tab)
        viewModelScope.launch {
            // API 호출 후 데이터 업데이트 로직
        }
    }

    private fun loadMVPData() {
        _uiState.value = MypageUiState.MVP(isLoading = true)
        viewModelScope.launch { /* 데이터 로드 */ }
    }

    private fun loadPredictionData() {
        _uiState.value = MypageUiState.Prediction(isLoading = true)
        viewModelScope.launch { /* 데이터 로드 */ }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            try {
                // TODO: 로그아웃 로직 (캐시 삭제 등)
                _event.emit(MypageEvent.NavigateHome)
            } catch (e: Exception) {
                _event.emit(MypageEvent.ShowErrorSnackbar(e))
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