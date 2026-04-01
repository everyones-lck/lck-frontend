package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeAlertsResponse(
    val alerts: List<HomeAlertsDetailResponse>,
    val alertCount: Int
)

@Serializable
data class HomeAlertsDetailResponse(
    val message: String,
    val matchId: Int,
    val team1Id: Int,
    val team1Name: String,
    val team2Id: Int,
    val team2Name: String
)