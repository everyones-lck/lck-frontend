package every.lol.com.core.network.model.response.aboutlck.team

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamPlayerHistoryResponse(
    val seasonDetails: List<SeasonDetailsResponse>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

@Serializable
data class SeasonDetailsResponse(
    val players: List<PlayerResponse>,
    val numberOfPlayerDetail: Int,
    val seasonName: String
)

@Serializable
data class PlayerResponse(
    val playerId: Int,
    val playerName: String,
    val PlayerRole: String,
    val playerPosition: String
)