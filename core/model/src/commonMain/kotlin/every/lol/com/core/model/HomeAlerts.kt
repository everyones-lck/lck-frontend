package every.lol.com.core.model

data class HomeAlerts(
    val alerts: List<HomeAlertsDetail>,
    val alertCount: Int
)

data class HomeAlertsDetail(
    val message: String,
    val matchId: Int,
    val team1Id: Int,
    val team1Name: String,
    val team2Id: Int,
    val team2Name: String
)