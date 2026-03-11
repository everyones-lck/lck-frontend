package every.lol.com.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

class AuthPreferences(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_EXPIRATION_TIME_KEY = stringPreferencesKey("access_token_expiration_time")
        val REFRESH_TOKEN_EXPIRATION_TIME_KEY = stringPreferencesKey("refresh_token_expiration_time")
    }

    suspend fun saveToken(accessToken: String, refreshToken: String, accessTokenExpirationTime: String, refreshTokenExpirationTime: String) {
        dataStore.edit {
            it[ACCESS_TOKEN_KEY] = accessToken
            it[REFRESH_TOKEN_KEY] = refreshToken
            it[ACCESS_TOKEN_EXPIRATION_TIME_KEY] = accessTokenExpirationTime
            it[REFRESH_TOKEN_EXPIRATION_TIME_KEY] = refreshTokenExpirationTime
        }
    }
}