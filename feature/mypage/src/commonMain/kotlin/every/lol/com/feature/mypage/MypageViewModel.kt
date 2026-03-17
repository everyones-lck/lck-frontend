package every.lol.com.feature.mypage

import every.lol.com.core.domain.usecase.GetProfileUseCase
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.model.CommentsDetail
import every.lol.com.core.model.PostsDetail
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
    private val nicknameUseCase: NicknameUseCase,
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MypageUiState>(MypageUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MypageEvent>()
    val event = _event.asSharedFlow()

    private val _appVersion = MutableStateFlow("1.0.0")
    val appVersion = _appVersion.asStateFlow()

    private var cachedMyInform: MypageUiState.MyInform? = null

    init {
        onIntent(MypageIntent.LoadInitial)
    }
    fun onIntent(intent: MypageIntent) {
        when (intent) {
            MypageIntent.LoadInitial -> checkInitialState()
            MypageIntent.LoadMypage -> loadMypageData()
            MypageIntent.LoadAppInform -> loadAppInformData()
            MypageIntent.LoadProfileEdit -> loadMypageData()
            MypageIntent.LoadMVP -> loadMVPData()
            MypageIntent.LoadPrediction -> loadPredictionData()
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
            getProfileUseCase().onSuccess { userInform ->
                val myInform = MypageUiState.MyInform(
                    nickName = userInform.nickname,
                    teamId = userInform.teamId.mapNotNull { id ->
                        Team.fromId(id)
                    }.toSet(),
                    profileImage = userInform.profileImage
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

            }.onFailure { throwable ->
                // 에러 처리
                _event.emit(MypageEvent.ShowErrorSnackbar(throwable))
            }
        }
    }

    private fun loadAppInformData() {
        setAppVersion()

        val appInfoMenus = listOf(
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_1, "개인정보 처리방침"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.TOS_2, "서비스 이용약관"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.OPEN_SOURCE_LICENCE, "오픈소스 라이선스"),
            MypageUiState.MypageMenu(MypageUiState.MypageMenuType.APP_VERSION, "앱버전 (${appVersion.value})", showDivider = false)
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
            val dummyPosts = listOf(
                PostsDetail(id = 1, title = "오늘 T1 경기 보신 분? 역대급이네", postType = "자유게시판"),
                PostsDetail(id = 2, title = "솔랭 꿀챔 추천해줌 (현직 다이아)", postType = "공략게시판"),
                PostsDetail(id = 3, title = "이거 버그 아님? 판정 왜이래", postType = "질문게시판")
            )

            val dummyComments = listOf(
                CommentsDetail(
                    commentId = 1,
                    postId = 1,
                    content = "ㄹㅇㅋㅋ 페이커 무빙 미쳤음",
                    postType = "자유게시판"
                ),
                CommentsDetail(
                    commentId = 2,
                    postId = 2,
                    content = "이거 보고 연패 끊었습니다 감사합니다",
                    postType = "공략게시판"
                )
            )
            _uiState.update { state ->
                if (state is MypageUiState.Community) {
                    state.copy(
                        isLoading = false,
                        posts = if (tab == MypageUiState.CommunityTab.POST) dummyPosts else emptyList(),
                        comments = if (tab == MypageUiState.CommunityTab.COMMENT) dummyComments else emptyList()
                    )
                } else state
            }
        }
    }

    private fun checkInitialState(){
        viewModelScope.launch {
            loadMypageData()
        }
    }

    private fun loadMVPData() {
        _uiState.value = MypageUiState.MVP(isLoading = true)
        viewModelScope.launch {
            val dummyMvpList = listOf(
                MypageUiState.MVPItem(
                    id = 1,
                    matchDate = "2024.03.15",
                    playerName = "Faker",
                    playerTeam = 1
                ),
                MypageUiState.MVPItem(
                    id = 2,
                    matchDate = "2024.03.14",
                    playerName = "Chovy",
                    playerTeam = 2
                )
            )
            _uiState.value = MypageUiState.MVP(
                mvpList = dummyMvpList,
                isLoading = false
            )
        }
    }

    private fun loadPredictionData() {
        _uiState.value = MypageUiState.Prediction(isLoading = true)
        viewModelScope.launch {
            val dummyPredictions = listOf(
                MypageUiState.PredictionItem(
                    id = 101,
                    matchDate = "2024.03.16",
                    homeTeamId = 3, // T1
                    awayTeamId = 2, // GEN
                    myVoteTeamId = 3, // 내가 T1 투표
                    winnerTeamId = 3,
                ),
                MypageUiState.PredictionItem(
                    id = 102,
                    matchDate = "2024.03.17",
                    homeTeamId = 1, // HLE
                    awayTeamId = 10, // KT
                    myVoteTeamId = 1, // 내가 HLE 투표
                    winnerTeamId = null,
                )
            )
            _uiState.value = MypageUiState.Prediction(
                rank = 10,
                predictions = dummyPredictions,
                isLoading = false
            )
        }
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

    private fun setAppVersion(){
        _appVersion.value = "1.0.0"
    }
}