package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchVoteRateResponse(
    val matchId: Long,
    val team1: MatchVoteTeamResponse,
    val team2: MatchVoteTeamResponse,
    val totalVoteCount: Int
)

@Serializable
data class MatchVoteTeamResponse(
    val teamId: Int,
    val voteCount: Int,
    val voteRate: Double
)