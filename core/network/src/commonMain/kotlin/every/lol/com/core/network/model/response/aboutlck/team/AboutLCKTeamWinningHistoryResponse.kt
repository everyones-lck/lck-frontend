package every.lol.com.core.network.model.response.aboutlck.team

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamWinningHistoryResponse(
    val seasonNameList: List<String>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)