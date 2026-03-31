package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val nickname: String,
    val profileImageUrl: String,
    val teamIds: List<Int>,
    val tier: String
)