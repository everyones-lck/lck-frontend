package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpirationTime: String,
    val refreshTokenExpirationTime: String,
)