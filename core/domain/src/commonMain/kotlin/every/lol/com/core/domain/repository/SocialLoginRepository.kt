package every.lol.com.core.domain.repository

interface SocialLoginRepository {
    suspend fun loginWithKakao(): Result<String>
}