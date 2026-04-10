package every.lol.com.feature.mypage

import every.lol.com.core.common.compressImage
import every.lol.com.core.domain.usecase.GetMyCommentsUseCase
import every.lol.com.core.domain.usecase.GetMyPostsUseCase
import every.lol.com.core.domain.usecase.GetProfileUseCase
import every.lol.com.core.domain.usecase.LogoutUseCase
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.domain.usecase.PatchMyTeamUseCase
import every.lol.com.core.domain.usecase.PatchProfileUseCase
import every.lol.com.core.domain.usecase.WithdrawalUseCase
import every.lol.com.core.domain.usecase.mypage.GetMyPogUseCase
import every.lol.com.core.domain.usecase.mypage.GetMyPomUseCase
import every.lol.com.core.domain.usecase.mypage.GetMyPredictionsUseCase
import every.lol.com.core.model.Team
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


sealed class MypageEvent {
    data object Logout: MypageEvent()
    data object Withdrawal: MypageEvent()
    data object NavigateHome : MypageEvent()
    data object NavigateProfileEdit: MypageEvent()
    data object NavigateToMyPosts : MypageEvent()
    data object NavigateToPogVote : MypageEvent()
    data object NavigateToPrediction : MypageEvent()
    data object NavigateWithdrawal: MypageEvent()
    data object NavigateTos1: MypageEvent()
    data object NavigateTos2: MypageEvent()
    data class NavigateToCommentDetail(val postId: Int) : MypageEvent()
    data class NavigateToPostDetail(val postId: Int): MypageEvent()
    //data object NavigateOpenSourceLicense: MypageEvent()
    data class ShowErrorSnackbar(val throwable: Throwable) : MypageEvent()
}

class MypageViewModel(
    private val nicknameUseCase: NicknameUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val patchProfileUseCase: PatchProfileUseCase,
    private val patchMyTeamUseCase: PatchMyTeamUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val getMyCommentsUseCase: GetMyCommentsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
    private val getMyPredictionsUseCase: GetMyPredictionsUseCase,
    private val getMyPomUseCase: GetMyPomUseCase,
    private val getMyPogUseCase: GetMyPogUseCase,
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
            MypageIntent.SaveProfile -> saveProfile()
            is MypageIntent.InputNickName -> handleInputNickName(intent.nickName)
            is MypageIntent.ClickCheckDuplicateNickname -> checkNicknameDuplicate(intent.nickName)
            is MypageIntent.Withdrawal -> handleWithdrawal()
            is MypageIntent.NavigateToCommentDetail -> navToCommentDetail(intent.postId, intent.commentId)
            is MypageIntent.NavigateToPostDetail -> navToPostDetail(intent.postId)
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
                    teamIds = userInform.teamIds.mapNotNull { id ->
                        Team.fromTeamId(id)
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

    private fun loadProfileEditData() {
        val currentInfo = cachedMyInform?: return

        val editState = MypageUiState.ProfileEdit(
            originalNickname = currentInfo.nickName,
            nickName = "",
            originalProfileImage = (currentInfo.profileImage as? ByteArray)?.copyOf() ?: currentInfo.profileImage,
            profileImage = (currentInfo.profileImage as? ByteArray)?.copyOf() ?: currentInfo.profileImage,
            originalTeamId = currentInfo.teamIds.toSet(),
            teamId = currentInfo.teamIds.toSet(),
            isDuplicateChecked = true,
            isLoading = false
        )

        _uiState.value = editState
    }

    fun handleInputNickName(name: String) {
        _uiState.update { state ->
            if (state is MypageUiState.ProfileEdit) {
                state.copy(
                    nickName = name,
                    isDuplicateChecked = name.isNotEmpty() || name == state.originalNickname
                )
            } else state
        }
    }

    fun checkNicknameDuplicate(name: String) {
        if (name.isBlank()) return
        val currentState = _uiState.value as? MypageUiState.ProfileEdit
        if (name == currentState?.originalNickname) {
            _uiState.update { state ->
                if (state is MypageUiState.ProfileEdit) state.copy(isDuplicateChecked = true) else state
            }
            return
        }
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

    fun updateProfileImage(newImage: Any?) {
        _uiState.update { state ->
            println("updateProfileImage: $newImage")
            if (state is MypageUiState.ProfileEdit) {
                state.copy(profileImage = newImage)
            } else state
        }
    }

    fun updateSelectedTeams(teams: Set<Team>) {
        _uiState.update { state ->
            if (state is MypageUiState.ProfileEdit) {
                state.copy(teamId = teams.toSet())
            } else state
        }
    }


    private fun saveProfile() {
        val current = _uiState.value as? MypageUiState.ProfileEdit ?: return

        val isNicknameChanged = current.nickName.isNotEmpty() && current.nickName != current.originalNickname

        if (isNicknameChanged && !current.isDuplicateChecked) {
            viewModelScope.launch {
                _event.emit(MypageEvent.ShowErrorSnackbar(Throwable("닉네임 중복 체크를 해주세요.")))
            }
            return
        }

        val isImageChanged = when {
            current.profileImage is ByteArray -> true
            current.profileImage is String && !(current.profileImage as String).startsWith("http") -> true
            current.profileImage == null && current.originalProfileImage != null -> true
            else -> false
        }

        val isTeamChanged = current.teamId != current.originalTeamId

        if (!isNicknameChanged && !isImageChanged && !isTeamChanged) {
            loadMypageData()
            return
        }

        viewModelScope.launch {
            _uiState.update { if (it is MypageUiState.ProfileEdit) it.copy(isLoading = true) else it }

            try {
                if (isTeamChanged) {
                    patchMyTeamUseCase(current.teamId.map { it.id }).getOrThrow()
                }

                if (isNicknameChanged || isImageChanged) {
                    val compressedImage = withContext(Dispatchers.IO) {
                        (current.profileImage as? ByteArray)?.compressImage(30)
                    }

                    patchProfileUseCase(
                        nickname = if (isNicknameChanged) current.nickName else null,
                        profileImage = compressedImage
                    ).getOrThrow()
                }

                loadMypageData()

            } catch (e: Exception) {
                _uiState.update { if (it is MypageUiState.ProfileEdit) it.copy(isLoading = false) else it }
                _event.emit(MypageEvent.ShowErrorSnackbar(e))
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
    private fun loadCommunityData(tab: MypageUiState.CommunityTab = MypageUiState.CommunityTab.POST) {
        _uiState.value = MypageUiState.Community(isLoading = true, selectedTab = tab)

        viewModelScope.launch {
            val page = 0
            when (tab) {
                MypageUiState.CommunityTab.POST -> {
                    getMyPostsUseCase(page).onSuccess { posts ->
                        _uiState.update { state ->
                            if (state is MypageUiState.Community) {
                                state.copy(
                                    isLoading = false,
                                    posts = posts.posts,
                                    comments = emptyList()
                                )
                            } else state
                        }
                    }.onFailure { throwable ->
                        handleCommunityError(throwable)
                    }
                }
                MypageUiState.CommunityTab.COMMENT -> {
                    getMyCommentsUseCase(page).onSuccess { comments ->
                        _uiState.update { state ->
                            if (state is MypageUiState.Community) {
                                state.copy(
                                    isLoading = false,
                                    posts = emptyList(),
                                    comments = comments.comments
                                )
                            } else state
                        }
                    }.onFailure { throwable ->
                        handleCommunityError(throwable)
                    }
                }
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
            getMyPomUseCase().onSuccess { response ->
                _uiState.value = MypageUiState.MVP(
                    mvpList = response.mvpVoteDetails.map { detail ->
                        MypageUiState.MVPItem(
                            detail.matchId,
                            detail.voteDate,
                            detail.playerName,
                            detail.playerId
                        )
                    },
                    isLoading = false
                )
            }
            getMyPogUseCase().onSuccess { response ->
                _uiState.value = MypageUiState.MVP(
                    mvpList = response.setPogVoteDetails.map { detail ->
                        MypageUiState.MVPItem(
                            detail.matchId,
                            detail.voteDate,
                            detail.playerName,
                            detail.playerId
                        )
                    },
                    isLoading = false
                )
            }
        }
    }

    private fun loadPredictionData() {
        _uiState.value = MypageUiState.Prediction(isLoading = true)
        viewModelScope.launch {
            getMyPredictionsUseCase().onSuccess { response ->
                _uiState.value = MypageUiState.Prediction(
                    rank = response.topPercent.toInt(),
                    data = response.predictionDetails.map { detail ->
                        MypageUiState.PredictionItem(
                            detail.matchId,
                            detail.matchDate,
                            detail.team1Name,
                            detail.team2Name,
                            detail.predictedTeamName,
                            detail.isPredictionSuccessful
                        ) },
                    isLoading = false
                )
            }.onFailure { error ->
                _event.emit(MypageEvent.ShowErrorSnackbar(error))
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _event.emit(MypageEvent.Logout)
            }
        }
    }

    private fun handleWithdrawal() {
        viewModelScope.launch {
            withdrawalUseCase().onSuccess {
                _event.emit(MypageEvent.Withdrawal)
            }
        }
    }

    private fun navToCommentDetail(postId: Int, commentId: Int) {
        viewModelScope.launch {
            _event.emit(MypageEvent.NavigateToCommentDetail(postId))
        }
    }

    private fun navToPostDetail(postId: Int) {
        viewModelScope.launch {
            _event.emit(MypageEvent.NavigateToPostDetail(postId))
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

    private suspend fun handleCommunityError(throwable: Throwable) {
        _uiState.update { state ->
            if (state is MypageUiState.Community) {
                state.copy(isLoading = false)
            } else state
        }
        _event.emit(MypageEvent.ShowErrorSnackbar(throwable))
    }
}