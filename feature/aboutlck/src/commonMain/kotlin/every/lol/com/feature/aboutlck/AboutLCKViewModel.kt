package every.lol.com.feature.aboutlck

import every.lol.com.core.common.formatMillisToDate
import every.lol.com.core.domain.usecase.GetHomeRankingUseCase
import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetMatchesUseCase
import every.lol.com.core.domain.usecase.GetSupportTeamUseCase
import every.lol.com.core.domain.usecase.aboutlck.GetAboutLCKMatchUseCase
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.Ranking
import every.lol.com.core.model.aboutlck.match.AboutLCKMatch
import every.lol.com.feature.aboutlck.model.AboutLCKIntent
import every.lol.com.feature.aboutlck.model.AboutLCKUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Clock

sealed interface AboutLCKEvent{
    data class ShowToast(val message: String): AboutLCKEvent
}


class AboutLCKViewModel(
    private val getAboutLCKMatchUseCase: GetAboutLCKMatchUseCase,
    private val getHomeRankingUseCase: GetHomeRankingUseCase,
    private val getSupportTeamUseCase: GetSupportTeamUseCase,
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getMatchVoteRateUseCase: GetMatchVoteRateUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<AboutLCKUiState>(AboutLCKUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AboutLCKEvent>(replay = 0)
    val event = _event.asSharedFlow()

    private var cachedMatches: List<MatchCardModel>? = null
    private var cachedExpandedIndex: Int = -1
    private var isFetching = false

    private var cachedRanking: Ranking? = null
    private var cachedMatch: AboutLCKMatch? = null
    private var cachedSupportTeam: List<Int>? = null
    private var cachedDate: String? = null
    private var isInitialLoaded = false

    init {
        onIntent(AboutLCKIntent.LoadInitial)
    }

    fun onIntent(intent: AboutLCKIntent) {
        when (intent) {
            AboutLCKIntent.LoadInitial -> loadInitial()
            AboutLCKIntent.RefreshHome -> loadMatches(isRefresh = true)
            is AboutLCKIntent.Match -> loadMatchPage(intent.matchId, intent.matchData)
            is AboutLCKIntent.ChangeDate -> loadDate(intent.date)
            is AboutLCKIntent.Team -> {} // TODO: Team 화면 전환 로직 연결
            is AboutLCKIntent.Player -> {} // TODO: Player 화면 전환 로직 연결
        }
    }

    private fun loadInitial() {

        _uiState.update { state ->
            if (state is AboutLCKUiState.AboutLCK) {
                state
            } else {
                AboutLCKUiState.AboutLCK(
                    ranking = cachedRanking,
                    match = cachedMatch,
                    supportTeam = cachedSupportTeam ?: emptyList(),
                    date = cachedDate ?: ""
                )
            }
        }

        if (isInitialLoaded) return

        isInitialLoaded = true
        loadDate()
        loadRanking()
        loadMatches()
    }

    private fun loadDate(date: String? = null) {
        val targetDate = date ?: formatMillisToDate(Clock.System.now().toEpochMilliseconds())
        cachedDate = targetDate

        _uiState.update { state ->
            val currentState = state as? AboutLCKUiState.AboutLCK ?: AboutLCKUiState.AboutLCK()
            currentState.copy(
                isLoading = true,
                date = targetDate
            )
        }
        getMatch(targetDate)
    }

    private fun loadMatchPage(matchId: Int, matchData: MatchCardModel) {
        _uiState.update {
            AboutLCKUiState.Match(
                matchId = matchId,
                matchData = matchData,
                isLoading = false
            )
        }
    }
    private fun getMatch(date:String){
        viewModelScope.launch {
            getAboutLCKMatchUseCase(date).onSuccess { result ->
                cachedMatch = result
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(
                        isLoading = false,
                        match = result
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(AboutLCKEvent.ShowToast("잠시 후 다시 시도해주세요."))
            }
        }
    }

    private fun loadMatches(isRefresh: Boolean = false) {
        if (isFetching) return

        if (!isRefresh && cachedMatches != null) {
            updateAboutLCKState { it.copy(isLoading = false) }
            return
        }

        isFetching = true
        updateLoading(true)

        viewModelScope.launch {
            getMatchesUseCase().onSuccess { matchInfo ->
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
                        },
                        actualWinnerTeamName = when {
                            match.team1.winner -> match.team1.teamName
                            match.team2.winner -> match.team2.teamName
                            else -> null
                        }
                    )
                }

                cachedMatches = matchCards
                cachedExpandedIndex = if (matchCards.isNotEmpty()) 0 else -1

                updateAboutLCKState { it.copy(isLoading = false) }
                isFetching = false
            }.onFailure {
                isFetching = false
                updateLoading(false)
            }
        }
    }


    private fun loadRanking() {
        viewModelScope.launch {
            getHomeRankingUseCase().onSuccess { result ->
                getSupportTeamUseCase().onSuccess { supportTeam ->
                    cachedRanking = result
                    cachedSupportTeam = supportTeam
                    _uiState.update { state ->
                        val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                        currentState.copy(
                            isLoading = false,
                            ranking = result,
                            supportTeam = supportTeam
                        )
                    }
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    val currentState = state as? AboutLCKUiState.AboutLCK ?:AboutLCKUiState.AboutLCK()
                    currentState.copy(isLoading = false)
                }
                isInitialLoaded = false
                println(error)
                _event.emit(AboutLCKEvent.ShowToast("잠시 후 다시 시도해주세요."))
            }
        }

    }
    private fun updateAboutLCKState(transform: (AboutLCKUiState.AboutLCK) -> AboutLCKUiState.AboutLCK) {
        _uiState.update { state ->
            val currentState = state as? AboutLCKUiState.AboutLCK ?: AboutLCKUiState.AboutLCK()
            transform(currentState)
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        updateAboutLCKState { it.copy(isLoading = isLoading) }
    }

}