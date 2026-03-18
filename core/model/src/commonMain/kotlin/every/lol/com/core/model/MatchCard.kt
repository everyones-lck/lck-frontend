package every.lol.com.core.model

data class MatchCardModel(
    val matchId: Long,
    val title: String,
    val team1Name: String,
    val team2Name: String,
    val matchName: String,
    val roundName: String
)