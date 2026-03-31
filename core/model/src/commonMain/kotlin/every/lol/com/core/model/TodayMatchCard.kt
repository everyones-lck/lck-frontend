package every.lol.com.core.model

enum class MatchStatus {
    BEFORE,
    LIVE,
    AFTER
}

data class TodayMatchCard(
    val matchId: Long,
    val title: String,
    val matchName: String,
    val roundName: String,
    val status: MatchStatus,

    val team1Name: String,
    val team2Name: String,

    val team1Rate: Float,
    val team2Rate: Float,

    val winner: WinnerSide? = null
)

enum class WinnerSide {
    TEAM1, TEAM2
}