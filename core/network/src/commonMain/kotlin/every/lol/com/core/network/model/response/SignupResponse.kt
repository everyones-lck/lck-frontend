package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class SignupResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpirationTime: String,
    val refreshTokenExpirationTime: String,
    val nickName: String
){
    //token 유출 대비
    override fun toString(): String = "TokenResponse(accessToken=***, refreshToken=***, accessTokenExpirationTime=$accessTokenExpirationTime, refreshTokenExpirationTime=$refreshTokenExpirationTime)"
}