package every.lol.com.feature.home.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.MatchCardModel

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Home(
        val matchCard: MatchCardModel? = null,
        val isRefreshing: Boolean = false
    ) : HomeUiState

    data class Error(
        val message: String = ""
    ) : HomeUiState
}

sealed interface HomeIntent {
    data object LoadInitial : HomeIntent
    data object RefreshHome : HomeIntent
    data class ClickMatchCard(val matchId: Long) : HomeIntent
}