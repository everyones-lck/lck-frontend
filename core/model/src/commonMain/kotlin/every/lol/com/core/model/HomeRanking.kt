package every.lol.com.core.model

data class HomeRanking(
    val groups: List<HomeRankingGroup>
)

data class HomeRankingGroup(
    val groupName: String,
    val teams: List<HomeRankingTeam>
)

data class HomeRankingTeam(
    val rank: Int,
    val teamName: String
)