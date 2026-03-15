package every.lol.com.core.model

data class Match(
    val matchByDateList: List<MatchByDateList>
)

data class MatchByDateList(
    val matchDetailList: List<MatchDetail>,
    val matchDate: String,
    val matchDetailSize: Int
)

data class MatchDetail(
    val team1: MatchTeam,
    val team2: MatchTeam,
    val matchFinished: Boolean,
    val season: String,
    val matchNumber: Int,
    val matchTime: MatchTime,
    val matchDate: String
)

data class MatchTeam(
    val teamName: String,
    val teamLogoUrl: String,
    val isWinner: Boolean
)

data class MatchTime(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)