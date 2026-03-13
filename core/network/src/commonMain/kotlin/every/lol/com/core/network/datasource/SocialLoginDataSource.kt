package every.lol.com.core.network.datasource

expect class SocialLoginDataSource {
    suspend fun loginWithKakao(): Result<String>
}