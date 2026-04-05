package every.lol.com.core.model

data class Ranking(
    val groups: List<RankingGroup>
)

data class RankingGroup(
    val groupName: String,
    val teams: List<RankingTeam>
)

data class RankingTeam(
    val rank: Int,
    val teamName: String
)