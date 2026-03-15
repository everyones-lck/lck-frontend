package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchResponse(
    val matchByDateList: List<MatchByDateListResponse>
)

@Serializable
data class MatchByDateListResponse(
    val matchDetailList: List<MatchDetailResponse>,
    val matchDate: String,
    val matchDetailSize: Int
)

@Serializable
data class MatchDetailResponse(
    val team1: TeamResponse,
    val team2: TeamResponse,
    val matchFinished: Boolean,
    val season: String,
    val matchNumber: Int,
    val matchTime: MatchTimeResponse,
    val matchDate: String
)

@Serializable
data class TeamResponse(
    val teamName: String,
    val teamLogoUrl: String,
    val isWinner: Boolean
)

@Serializable
data class MatchTimeResponse(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)