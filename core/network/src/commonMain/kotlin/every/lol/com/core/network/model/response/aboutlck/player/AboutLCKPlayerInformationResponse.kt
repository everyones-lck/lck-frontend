package every.lol.com.core.network.model.response.aboutlck.player

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKPlayerInformationResponse(
    val nickName: String,
    val realName: String,
    val birthDate: String,
    val position: String
)