package every.lol.com.feature.matches

import every.lol.com.core.domain.usecase.GetMatchPogCandidateUseCase
import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetMatchesCandidateUseCase
import every.lol.com.core.domain.usecase.GetMatchesUseCase
import every.lol.com.core.domain.usecase.GetSetPogCandidateUseCase
import every.lol.com.core.model.MatchCardModel
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MatchesViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getMatchVoteRateUseCase: GetMatchVoteRateUseCase,
    private val getMatchPogCandidateUseCase: GetMatchPogCandidateUseCase,
    private val getSetPogCandidateUseCase: GetSetPogCandidateUseCase,
    private val getMatchCandidateUseCase: GetMatchesCandidateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MatchUiState>(MatchUiState.Loading)

    val uiState: StateFlow<MatchUiState> = _uiState

    init {
        onIntent(MatchIntent.LoadMatches)
    }

    fun onIntent(intent: MatchIntent) {
        when (intent) {
            MatchIntent.LoadMatches -> {
                loadMatches()
            }

            is MatchIntent.ClickPrediction -> {
                _uiState.value = MatchUiState.Prediction(
                    matchId = intent.matchId,
                    isLoading = true
                )
                getMatchCandidate(intent.matchId)
                getMatchPogCandidate(intent.matchId)
                getSetPogCandidate(intent.matchId)
            }

            is MatchIntent.ClickLiveResult -> {
                _uiState.value = MatchUiState.LiveResult(
                    matchId = intent.matchId,
                    selectedTabIndex = 0
                )
            }

            MatchIntent.BackToMatches -> {
                val currentMatches = (_uiState.value as? MatchUiState.Matches)?.matches.orEmpty()
                _uiState.value = MatchUiState.Matches(
                    matches = currentMatches,
                    expandedIndex = 0
                )
            }


            MatchIntent.BackToPrediction -> {
                val current = _uiState.value as? MatchUiState.LiveResult ?: return
                _uiState.value = MatchUiState.Prediction(
                    matchId = current.matchId
                )
            }

            is MatchIntent.ToggleMatchCard -> {
                val current = _uiState.value as? MatchUiState.Matches ?: return
                val nextIndex =
                    if (current.expandedIndex == intent.index) -1 else intent.index

                _uiState.value = current.copy(
                    expandedIndex = nextIndex
                )
            }

            is MatchIntent.SelectLiveResultTab -> {
                val current = _uiState.value as? MatchUiState.LiveResult ?: return
                _uiState.value = current.copy(
                    selectedTabIndex = intent.index
                )
            }
        }
    }

    private fun loadMatches() {
        viewModelScope.launch {
            _uiState.value = MatchUiState.Loading

            val result = getMatchesUseCase()

            result.fold(
                onSuccess = { matchInfoResult ->
                    val matchCards = supervisorScope {
                        matchInfoResult.matchInfo.map { match ->
                            async {
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
                                        (voteRate?.team1?.voteRate
                                            ?: 0.0) > (voteRate?.team2?.voteRate
                                            ?: 0.0) -> match.team1.teamName

                                        (voteRate?.team2?.voteRate
                                            ?: 0.0) > (voteRate?.team1?.voteRate
                                            ?: 0.0) -> match.team2.teamName

                                        else -> null
                                    }
                                )
                            }
                        }.awaitAll()
                    }

                    _uiState.value = MatchUiState.Matches(
                        matches = matchCards,
                        expandedIndex = if (matchCards.isNotEmpty()) 0 else -1
                    )
                },
                onFailure = {
                    _uiState.value = MatchUiState.Matches(
                        matches = emptyList(),
                        expandedIndex = -1
                    )
                }
            )
        }
    }


    //승혁 코드
    private fun getMatchCandidate(matchId: Long) {
        viewModelScope.launch {
            getMatchCandidateUseCase(matchId).onSuccess {
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(
                        isLoading = false,
                        matchId = it.matchId,
                        matchData = it
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(isLoading = false)
                }
                println(error)
                //_event.emit(MatchEvent.ShowToast(error.message ?: "데이터를 불러오지 못했습니다."))
            }
        }
    }

    private fun getMatchPogCandidate(matchId: Long) {
        viewModelScope.launch {
            getMatchPogCandidateUseCase(matchId).onSuccess {
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(
                        isLoading = false,
                        matchPogData = it
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(isLoading = false)
                }
                println(error)
            }
        }
    }

    private fun getSetPogCandidate(matchId: Long) {
        viewModelScope.launch {
            getSetPogCandidateUseCase(matchId).onSuccess {
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(
                        isLoading = false,
                        setPogData = it.sets
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState =
                        state as? MatchUiState.Prediction ?: MatchUiState.Prediction()
                    currentState.copy(isLoading = false)
                }
                println(error)
            }
        }
    }


}