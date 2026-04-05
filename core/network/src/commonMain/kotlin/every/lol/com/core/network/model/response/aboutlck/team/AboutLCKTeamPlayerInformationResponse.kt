package every.lol.com.core.network.model.response.aboutlck.team

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamPlayerInformationResponse(
    val playerDetails: List<PlayerDetailsResponse>,
    val numberOfPlayerDetail: Int
)


@Serializable
data class PlayerDetailsResponse(
    val playerId: Int,
    val playerName: String,
    val playerRole: String,
)