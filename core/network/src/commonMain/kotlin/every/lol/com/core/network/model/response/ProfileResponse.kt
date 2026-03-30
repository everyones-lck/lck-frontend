package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val nickname: String,
    val profileImageUrl: String,
    val teamNames: List<String>,
    val tier: String
)