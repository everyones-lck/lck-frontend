package every.lol.com.feature.aboutlck.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AboutLCKUiState {
    data object Loading : AboutLCKUiState

    data class Home(
        val isLoading: Boolean = false
    ) : AboutLCKUiState
}

sealed interface AboutLCKIntent {
    data object LoadInitial : AboutLCKIntent
    data object RefreshHome : AboutLCKIntent
}