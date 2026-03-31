package every.lol.com.feature.matches.model

import every.lol.com.core.model.TodayMatchCard

sealed interface MatchUiState {
    data class Matches(
        val matches: List<TodayMatchCard>,
        val expandedIndex: Int = 0
    ) : MatchUiState

    data class Prediction(
        val matchId: Long
    ) : MatchUiState

    data class LiveResult(
        val matchId: Long,
        val selectedTabIndex: Int = 0
    ) : MatchUiState

    data object Loading : MatchUiState
}

sealed interface MatchIntent {
    data object LoadMatches : MatchIntent
    data class ClickPrediction(val matchId: Long) : MatchIntent
    data class ClickLiveResult(val matchId: Long) : MatchIntent
    data object BackToMatches : MatchIntent
    data object BackToPrediction : MatchIntent
    data class ToggleMatchCard(val index: Int) : MatchIntent
    data class SelectLiveResultTab(val index: Int) : MatchIntent
}