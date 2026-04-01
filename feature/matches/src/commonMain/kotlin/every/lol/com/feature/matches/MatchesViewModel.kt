package every.lol.com.feature.matches

import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetMatchesUseCase
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus
import every.lol.com.core.model.TodayMatchCard
import every.lol.com.core.model.WinnerSide
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MatchesViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getMatchVoteRateUseCase: GetMatchVoteRateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MatchUiState>(MatchUiState.Loading)
    val uiState = _uiState.asStateFlow()

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
}