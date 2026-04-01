package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchPogCandidateResponse(
    val matchId: Int,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidateResponse>,
    val myVotedPlayerId: Int
)