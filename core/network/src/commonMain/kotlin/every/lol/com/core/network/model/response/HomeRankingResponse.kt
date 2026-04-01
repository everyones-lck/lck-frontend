package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeRankingResponse(
    val groups: List<HomeRankingGroupResponse>
)

@Serializable
data class HomeRankingGroupResponse(
    val groupName: String,
    val teams: List<HomeRankingTeamResponse>
)

@Serializable
data class HomeRankingTeamResponse(
    val rank: Int,
    val teamName: String
)