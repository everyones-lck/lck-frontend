package every.lol.com.core.model.aboutlck.match

data class AboutLCKMatch(
    val matches: List<MatchDetail>
)

data class MatchDetail(
    val matchId: Int,
    val matchDate: String,
    val matchStatus: String,
    val seasonName: String,
    val groupName: String?=null,
    val roundName: String?=null,
    val team1: MatchTeam,
    val team2: MatchTeam,
)

data class MatchTeam(
    val teamId: Int,
    val teamName: String,
    val winner: Boolean
)