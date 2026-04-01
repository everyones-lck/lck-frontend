package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SetPogCandidateResponse(
    val sets: List<SetPogCandidateDetailResponse>
)

@Serializable
data class SetPogCandidateDetailResponse(
    val setIndex: Int,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidateResponse>,
    val myVotedPlayerId: Long ? = null
)

@Serializable
data class PogCandidateCandidateResponse(
    val playerId: Int,
    val playerName: String
)
