package every.lol.com.feature.matches

import every.lol.com.core.domain.usecase.GetMatchPogResultUseCase
import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetMatchesUseCase
import every.lol.com.core.domain.usecase.GetSetPogResultUseCase
import every.lol.com.core.domain.usecase.PostMatchPogVoteUseCase
import every.lol.com.core.domain.usecase.PostMatchVoteUseCase
import every.lol.com.core.domain.usecase.PostSetPogVoteUseCase
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus
import every.lol.com.core.model.SetPogVoteItem
import every.lol.com.core.model.TodayMatchCard
import every.lol.com.core.model.WinnerSide
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

sealed interface MatchEvent {
    data class ShowToast(val message: String) : MatchEvent
    data object VoteSuccess : MatchEvent
}

class MatchesViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getMatchVoteRateUseCase: GetMatchVoteRateUseCase,
    private val postMatchVoteUseCase: PostMatchVoteUseCase,
    private val postSetPogVoteUseCase: PostSetPogVoteUseCase,
    private val postMatchPogVoteUseCase: PostMatchPogVoteUseCase,
    private val getSetPogResultUseCase: GetSetPogResultUseCase,
    private val getMatchPogResultUseCase: GetMatchPogResultUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MatchUiState>(MatchUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MatchEvent>(replay = 0)
    val event = _event.asSharedFlow()

    private var cachedMatches: List<MatchCardModel> = emptyList()
    private var cachedExpandedIndex: Int = -1
    private var currentMatchId: Long? = null
    private var isFetching = false

    init {
        onIntent(MatchIntent.LoadMatches)
    }

    fun onIntent(intent: MatchIntent) {
        when (intent) {
            MatchIntent.LoadMatches -> loadMatches()

            is MatchIntent.ClickPrediction -> {
                currentMatchId = intent.matchId
                _uiState.value = MatchUiState.Prediction(
                    matchId = intent.matchId
                )
            }

            is MatchIntent.ClickLiveResult -> {
                currentMatchId = intent.matchId
                _uiState.value = MatchUiState.LiveResult(
                    matchId = intent.matchId,
                    selectedTabIndex = 0
                )
            }

            MatchIntent.BackToMatches -> {
                _uiState.value = MatchUiState.Matches(
                    matches = cachedMatches,
                    expandedIndex = cachedExpandedIndex
                )
            }

            MatchIntent.BackToPrediction -> {
                val matchId = currentMatchId ?: return
                _uiState.value = MatchUiState.Prediction(
                    matchId = matchId
                )
            }

            is MatchIntent.ToggleMatchCard -> {
                val currentState = _uiState.value as? MatchUiState.Matches ?: return
                val nextIndex =
                    if (currentState.expandedIndex == intent.index) -1 else intent.index

                cachedExpandedIndex = nextIndex

                _uiState.value = currentState.copy(
                    expandedIndex = nextIndex
                )
            }

            is MatchIntent.SelectLiveResultTab -> {
                val currentState = _uiState.value as? MatchUiState.LiveResult ?: return
                _uiState.value = currentState.copy(
                    selectedTabIndex = intent.index
                )
            }

            is MatchIntent.SubmitPredictionVote -> {
                handleSubmitPredictionVote(
                    matchId = intent.matchId,
                    teamId = intent.teamId
                )
            }

            is MatchIntent.SubmitSetPogVote -> {
                handleSubmitSetPogVote(
                    matchId = intent.matchId,
                    setPogVotes = intent.setPogVotes
                )
            }

            is MatchIntent.SubmitMatchPogVote -> {
                handleSubmitMatchPogVote(
                    matchId = intent.matchId,
                    playerId = intent.playerId
                )
            }

            is MatchIntent.LoadSetPogResult -> {
                handleLoadSetPogResult(intent.matchId)
            }

            is MatchIntent.LoadMatchPogResult -> {
                handleLoadMatchPogResult(intent.matchId)
            }
        }
    }
    private fun loadMatches(isRefresh: Boolean = false) {
        if (isFetching) return

        if (!isRefresh && cachedMatches.isNotEmpty()) {
            _uiState.value = MatchUiState.Matches(
                matches = cachedMatches,
                expandedIndex = cachedExpandedIndex
            )
            return
        }

        isFetching = true
        _uiState.value = MatchUiState.Loading

        viewModelScope.launch {
            getMatchesUseCase()
                .onSuccess { matchInfo ->
                    val matchCards = matchInfo.matchInfo.map { match ->
                        val voteRate = getMatchVoteRateUseCase(match.matchId).getOrNull()

                        MatchCardModel(
                            matchId = match.matchId,
                            matchDate = match.matchDate,
                            matchStatus = match.matchStatus,
                            seasonName = match.seasonName,
                            groupName = match.groupName,
                            roundName = match.roundName,
                            team1Id = match.team1.teamId,
                            team1Name = match.team1.teamName,
                            team2Id = match.team2.teamId,
                            team2Name = match.team2.teamName,
                            team1VoteRate = voteRate?.team1?.voteRate ?: 0.0,
                            team2VoteRate = voteRate?.team2?.voteRate ?: 0.0,
                            totalVoteCount = voteRate?.totalVoteCount ?: 0,
                            predictedWinnerTeamName = when {
                                (voteRate?.team1?.voteRate ?: 0.0) > (voteRate?.team2?.voteRate ?: 0.0) -> match.team1.teamName
                                (voteRate?.team2?.voteRate ?: 0.0) > (voteRate?.team1?.voteRate ?: 0.0) -> match.team2.teamName
                                else -> null
                            }
                        )
                    }

                    cachedMatches = matchCards
                    cachedExpandedIndex = if (matchCards.isNotEmpty()) 0 else -1

                    _uiState.value = MatchUiState.Matches(
                        matches = cachedMatches,
                        expandedIndex = cachedExpandedIndex
                    )

                    isFetching = false
                }
                .onFailure {
                    cachedMatches = emptyList()
                    cachedExpandedIndex = -1

                    _uiState.value = MatchUiState.Matches(
                        matches = emptyList(),
                        expandedIndex = -1
                    )

                    isFetching = false
                }
        }
    }

    private fun handleSubmitPredictionVote(
        matchId: Long,
        teamId: Int
    ) {
        viewModelScope.launch {
            postMatchVoteUseCase(
                matchId = matchId,
                teamId = teamId
            ).onSuccess {
                _event.emit(MatchEvent.VoteSuccess)
            }.onFailure { throwable ->
                _event.emit(
                    MatchEvent.ShowToast(
                        throwable.message ?: "투표에 실패했습니다."
                    )
                )
            }
        }
    }

    private fun handleSubmitSetPogVote(
        matchId: Long,
        setPogVotes: List<SetPogVoteItem>
    ) {
        viewModelScope.launch {
            postSetPogVoteUseCase(
                matchId = matchId,
                setPogVotes = setPogVotes
            ).onSuccess {
                _event.emit(MatchEvent.VoteSuccess)
            }.onFailure { throwable ->
                _event.emit(
                    MatchEvent.ShowToast(
                        throwable.message ?: "세트 POG 투표에 실패했습니다."
                    )
                )
            }
        }
    }

    private fun handleSubmitMatchPogVote(
        matchId: Long,
        playerId: Long?
    ) {
        viewModelScope.launch {
            postMatchPogVoteUseCase(
                matchId = matchId,
                playerId = playerId
            ).onSuccess {
                _event.emit(MatchEvent.VoteSuccess)
            }.onFailure { throwable ->
                _event.emit(
                    MatchEvent.ShowToast(
                        throwable.message ?: "매치 POM 투표에 실패했습니다."
                    )
                )
            }
        }
    }

    private fun handleLoadSetPogResult(matchId: Long) {
        val currentState = _uiState.value as? MatchUiState.LiveResult ?: return

        _uiState.value = currentState.copy(
            isLoading = true
        )

        viewModelScope.launch {
            getSetPogResultUseCase(matchId)
                .onSuccess { result ->
                    val latestState = _uiState.value as? MatchUiState.LiveResult ?: return@onSuccess
                    _uiState.value = latestState.copy(
                        setPogResult = result,
                        isLoading = false
                    )
                }
                .onFailure {
                    val latestState = _uiState.value as? MatchUiState.LiveResult ?: return@onFailure
                    _uiState.value = latestState.copy(
                        isLoading = false
                    )
                }
        }
    }

    private fun handleLoadMatchPogResult(matchId: Long) {
        val currentState = _uiState.value as? MatchUiState.LiveResult ?: return

        _uiState.value = currentState.copy(
            isLoading = true
        )

        viewModelScope.launch {
            getMatchPogResultUseCase(matchId)
                .onSuccess { result ->
                    val latestState = _uiState.value as? MatchUiState.LiveResult ?: return@onSuccess
                    _uiState.value = latestState.copy(
                        matchPogResult = result,
                        isLoading = false
                    )
                }
                .onFailure {
                    val latestState = _uiState.value as? MatchUiState.LiveResult ?: return@onFailure
                    _uiState.value = latestState.copy(
                        isLoading = false
                    )
                }
        }
    }
}