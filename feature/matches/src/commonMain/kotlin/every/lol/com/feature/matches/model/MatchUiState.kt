package every.lol.com.feature.matches.model

import every.lol.com.core.model.MatchCandidate
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchPogCandidate
import every.lol.com.core.model.MatchPogResult
import every.lol.com.core.model.SetPogCandidateDetail
import every.lol.com.core.model.SetPogResult
import every.lol.com.core.model.SetPogVoteItem

sealed interface MatchUiState {
    data class Matches(
        val matches: List<MatchCardModel>,
        val expandedIndex: Int = 0
    ) : MatchUiState

    data class Prediction(
        val match: MatchCardModel? = null,
        val isLoading: Boolean = false,
        val matchId: Long = 0,
        val matchData: MatchCandidate? = null,
        val matchPogData: MatchPogCandidate? = null,
        val setPogData: List<SetPogCandidateDetail>? = null,
        val seasonName: String ="",
        val groupName: String ="",
        val roundName: String ="",
        val matchDate: String ="",
        val isPogSaved: Boolean = false
    ) : MatchUiState

    data class LiveResult(
        val matchId: Long,
        val selectedTabIndex: Int = 0,
        val setPogResult: SetPogResult? = null,
        val matchPogResult: MatchPogResult? = null,
        val isLoading: Boolean = false
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
    data class SubmitPredictionVote(val matchId: Long, val teamId: Int) : MatchIntent
    data class SubmitSetPogVote(val matchId: Long, val setPogVotes: List<SetPogVoteItem>) : MatchIntent
    data class SubmitMatchPogVote(val matchId: Long, val playerId: Long?) : MatchIntent
    data class LoadSetPogResult(val matchId: Long) : MatchIntent
    data class LoadMatchPogResult(val matchId: Long) : MatchIntent
    data class SubmitPogVotes(
        val matchId: Long,
        val setPogVotes: List<SetPogVoteItem>,
        val matchPogPlayerId: Long?
    ) : MatchIntent
    data object RefreshMatches : MatchIntent
}