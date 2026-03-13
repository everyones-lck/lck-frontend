package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable


@Serializable
data class SignupRequest(
    val profileImage: ByteArray?=null,
    val signupUserData: SignupUserData
)

@Serializable
data class SignupUserData(
    val kakaoUserId: String,
    val nickName: String,
    val role: String,
    val tier: String,
    val teamId: Int
)