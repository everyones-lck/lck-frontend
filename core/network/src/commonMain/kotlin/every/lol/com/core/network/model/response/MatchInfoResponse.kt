package every.lol.com.core.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MatchStatus {
    SCHEDULED,
    LIVE,
    FINISHED
}

@Serializable
data class MatchInfoResponse(
    @SerialName("matches")
    val matchInfo: List<MatchesResponse>
)

@Serializable
data class MatchesResponse(
    val matchId: Long,
    val matchDate: String,
    val matchStatus: MatchStatus,
    val seasonName: String,
    val groupName: String? = null,
    val roundName: String,
    val team1: MatchTeamResponse,
    val team2: MatchTeamResponse
)

@Serializable
data class MatchTeamResponse(
    val teamId: Int,
    val teamName: String,
    val winner: Boolean
)