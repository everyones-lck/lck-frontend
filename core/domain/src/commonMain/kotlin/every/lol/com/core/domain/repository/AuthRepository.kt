package every.lol.com.core.domain.repository

interface AuthRepository {

    suspend fun login(kakaoUserId: String): Result<Unit>

}