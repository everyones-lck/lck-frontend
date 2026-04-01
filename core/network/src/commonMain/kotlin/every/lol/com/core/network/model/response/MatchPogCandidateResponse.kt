package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchPogCandidateResponse(
    val matchId: Long,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidateResponse>,
    val myVotedPlayerId: Long ? = null
)