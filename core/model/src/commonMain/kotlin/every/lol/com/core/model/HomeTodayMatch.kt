package every.lol.com.core.model

data class HomeTodayMatch(
    val matches: List<HomeTodayMatchDetail>
)

data class HomeTodayMatchDetail(
    val matchId: Long,
    val matchDate: String,
    val matchStatus: String,
    val seasonName: String,
    val groupName: String,
    val roundName: String,
    val team1: HomeTeam,
    val team2: HomeTeam
)

data class HomeTeam(
    val teamId: Long,
    val teamName: String,
    val winner: Boolean
)