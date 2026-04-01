package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeTodayMatchResponse(
    val matches: List<HomeTodayMatchDetailResponse>
)

@Serializable
data class HomeTodayMatchDetailResponse(
    val matchId: Long,
    val matchDate: String,
    val matchStatus: String,
    val seasonName: String,
    val groupName: String?,
    val roundName: String,
    val team1: HomeTeamResponse,
    val team2: HomeTeamResponse
)

@Serializable
data class HomeTeamResponse(
    val teamId: Long,
    val teamName: String,
    val winner: Boolean
)