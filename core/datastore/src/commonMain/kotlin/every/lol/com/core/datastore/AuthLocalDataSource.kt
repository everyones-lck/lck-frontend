package every.lol.com.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AuthLocalDataSource(
    private val authPreferences: AuthPreferences
) {

    suspend fun saveToken(accessToken: String, refreshToken: String, accessTokenExpirationTime: String, refreshTokenExpirationTime: String) {
        authPreferences.saveToken(accessToken, refreshToken, accessTokenExpirationTime, refreshTokenExpirationTime)
        print("토큰 저장")
    }

    suspend fun saveUserId(kakaoUserId: String){
        authPreferences.saveUserId(kakaoUserId)
    }

    suspend fun clearAuthData() {
        authPreferences.clearAuthData()
        authPreferences.clearUserId()
        authPreferences.clearSupportTeam()
    }

    suspend fun getAuthData() = authPreferences.authData.firstOrNull()

    val supportTeamIds: Flow<List<Int>> = authPreferences.supportTeamIds

    suspend fun saveSupportTeam(teamIds: List<Int>) {
        authPreferences.saveSupportTeam(teamIds)
    }

    suspend fun getSupportTeamIdsOnce() = authPreferences.supportTeamIds.firstOrNull()
}