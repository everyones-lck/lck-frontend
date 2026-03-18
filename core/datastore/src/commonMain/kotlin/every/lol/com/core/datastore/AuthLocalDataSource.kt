package every.lol.com.core.datastore

import kotlinx.coroutines.flow.firstOrNull

class AuthLocalDataSource(
    private val authPreferences: AuthPreferences
) {

    suspend fun saveToken(accessToken: String, refreshToken: String, accessTokenExpirationTime: String, refreshTokenExpirationTime: String) {
        authPreferences.saveToken(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime)
        println("로그: 토큰 저장 완료")
    }

    suspend fun saveUserId(kakaoUserId: String){
        authPreferences.saveUserId(kakaoUserId)
    }

    suspend fun clearAuthData() {
        authPreferences.clearAuthData()
    }

    suspend fun getAuthData() = authPreferences.authData.firstOrNull()
}