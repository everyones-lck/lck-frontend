package every.lol.com.feature.aboutlck.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.Ranking
import every.lol.com.core.model.aboutlck.match.AboutLCKMatch

@Immutable
sealed interface AboutLCKUiState {
    data object Loading : AboutLCKUiState

    data class AboutLCK(
        val isLoading: Boolean = false,
        val ranking: Ranking?= null,
        val match: AboutLCKMatch?= null
        ) : AboutLCKUiState
}

sealed interface AboutLCKIntent {
    data object LoadInitial : AboutLCKIntent
    data object RefreshHome : AboutLCKIntent
}