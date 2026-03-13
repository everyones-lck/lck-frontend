package every.lol.com.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences(
    private val dataStore: DataStore<Preferences>
) {

    val authData: Flow<AuthData> = dataStore.data.map { preferences ->
        AuthData(
            kakaoUserId = preferences[KAKAO_USER_ID],
            accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
            refreshToken = preferences[REFRESH_TOKEN_KEY] ?: "",
            accessTokenExpirationTime = preferences[ACCESS_TOKEN_EXPIRATION_TIME_KEY] ?: "",
            refreshTokenExpirationTime = preferences[REFRESH_TOKEN_EXPIRATION_TIME_KEY] ?: ""
        )
    }

    companion object {

        val KAKAO_USER_ID = stringPreferencesKey("kakao_user_id")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_EXPIRATION_TIME_KEY = stringPreferencesKey("access_token_expiration_time")
        val REFRESH_TOKEN_EXPIRATION_TIME_KEY = stringPreferencesKey("refresh_token_expiration_time")
    }

    suspend fun saveUserId(kakaoUserId: String){
        dataStore.edit {
            it[KAKAO_USER_ID] = kakaoUserId
        }
    }
    suspend fun clearUserId(){
        dataStore.edit {
            it.remove(KAKAO_USER_ID)
        }
    }
    suspend fun saveToken(accessToken: String, refreshToken: String, accessTokenExpirationTime: String, refreshTokenExpirationTime: String) {
        dataStore.edit {
            it[ACCESS_TOKEN_KEY] = accessToken
            it[REFRESH_TOKEN_KEY] = refreshToken
            it[ACCESS_TOKEN_EXPIRATION_TIME_KEY] = accessTokenExpirationTime
            it[REFRESH_TOKEN_EXPIRATION_TIME_KEY] = refreshTokenExpirationTime
        }
    }
    suspend fun clearAuthData() {
        dataStore.edit {
            it.clear()
        }
    }
}

data class AuthData(
    val kakaoUserId: String?=null,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpirationTime: String,
    val refreshTokenExpirationTime: String
)