package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileResponse(
    val updatedProfileImageUrl: String,
    val updatedNickname: String
)