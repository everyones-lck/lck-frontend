package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileRequest(
    val profileImage: ByteArray?=null,
    val request: PatchProfileData
)

@Serializable
data class PatchProfileData(
    val nickname: String?=null,
    val isDefaultImage: Boolean
)