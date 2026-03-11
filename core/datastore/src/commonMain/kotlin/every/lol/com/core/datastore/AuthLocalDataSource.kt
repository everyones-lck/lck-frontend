package every.lol.com.core.datastore

class AuthLocalDataSource(
    private val authPreferences: AuthPreferences
) {

    suspend fun saveToken(accessToken: String, refreshToken: String, accessTokenExpirationTime: String, refreshTokenExpirationTime: String) {
        authPreferences.saveToken(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime)
    }

}