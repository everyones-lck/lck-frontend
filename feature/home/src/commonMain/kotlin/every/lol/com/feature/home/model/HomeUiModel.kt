package every.lol.com.feature.home.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeRanking
import every.lol.com.core.model.HomeTodayMatch

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Home(
        val isLoading: Boolean = false,
        val matches: HomeTodayMatch ?= null,
        val ranking: HomeRanking?= null,
        val news: HomeNews?=null,
        val isRefreshing: Boolean = false
    ) : HomeUiState

    data class Error(
        val message: String = ""
    ) : HomeUiState
}

sealed interface HomeIntent {
    data object LoadInitial : HomeIntent
    data object RefreshHome : HomeIntent
    data object LoadTodayMatchHome : HomeIntent
    data class ClickMatchCard(val matchId: Long) : HomeIntent
}