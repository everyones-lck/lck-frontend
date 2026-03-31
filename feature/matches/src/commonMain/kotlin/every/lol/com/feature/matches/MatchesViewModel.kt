package every.lol.com.feature.matches

import every.lol.com.core.model.MatchStatus
import every.lol.com.core.model.TodayMatchCard
import every.lol.com.core.model.WinnerSide
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class MatchesViewModel : ViewModel() {
    private val dummyMatches = listOf(
        TodayMatchCard(
            matchId = 1L,
            title = "2026 Road to MSI",
            matchName = "Baron Elder",
            roundName = "플레이오프 1라운드",
            status = MatchStatus.BEFORE,
            team1Name = "HLE",
            team2Name = "GEN",
            team1Rate = 0.28f,
            team2Rate = 0.72f,
            winner = null
        ),
        TodayMatchCard(
            matchId = 2L,
            title = "2026 Road to MSI",
            matchName = "Baron Elder",
            roundName = "플레이오프 1라운드",
            status = MatchStatus.LIVE,
            team1Name = "GEN.G",
            team2Name = "T1",
            team1Rate = 0.45f,
            team2Rate = 0.55f,
            winner = null
        ),
        TodayMatchCard(
            matchId = 3L,
            title = "2026 Road to MSI",
            matchName = "Baron Elder",
            roundName = "플레이오프 1라운드",
            status = MatchStatus.AFTER,
            team1Name = "GEN.G",
            team2Name = "T1",
            team1Rate = 0.35f,
            team2Rate = 0.65f,
            winner = WinnerSide.TEAM2
        )
    )
    private val _uiState = MutableStateFlow<MatchUiState>(MatchUiState.Loading)

    val uiState: StateFlow<MatchUiState> = _uiState

    init {
        onIntent(MatchIntent.LoadMatches)
    }

    fun onIntent(intent: MatchIntent) {
        when (intent) {
            MatchIntent.LoadMatches -> {
                _uiState.value = MatchUiState.Matches(
                    matches = dummyMatches,
                    expandedIndex = 0
                )
            }

            is MatchIntent.ClickPrediction -> {
                _uiState.value = MatchUiState.Prediction(
                    matchId = intent.matchId
                )
            }

            is MatchIntent.ClickLiveResult -> {
                _uiState.value = MatchUiState.LiveResult(
                    matchId = intent.matchId,
                    selectedTabIndex = 0
                )
            }

            MatchIntent.BackToMatches -> {
                _uiState.value = MatchUiState.Matches(
                    matches = dummyMatches,
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
}