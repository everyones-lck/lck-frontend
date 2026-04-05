package every.lol.com.core.network.model.response.aboutlck.match

import kotlinx.serialization.Serializable

@Serializable
data class AboutLCKMatchResponse(
    val matches: List<MatchDetailResponse>
)

@Serializable
data class MatchDetailResponse(
    val matchId: Int,
    val matchDate: String,
    val matchStatus: String,
    val seasonName: String,
    val groupName: String?=null,
    val roundName: String?=null,
    val team1: TeamResponse,
    val team2: TeamResponse,
)

@Serializable
data class TeamResponse(
    val teamId: Int,
    val teamName: String,
    val winner: Boolean
)