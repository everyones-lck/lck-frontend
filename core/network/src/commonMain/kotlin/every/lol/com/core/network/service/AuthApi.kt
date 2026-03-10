package every.lol.com.core.network.service

interface AuthApi {
    suspend fun login(kakaoUserId: String)
}