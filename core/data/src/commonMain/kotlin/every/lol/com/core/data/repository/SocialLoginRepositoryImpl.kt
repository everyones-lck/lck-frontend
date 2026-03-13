package every.lol.com.core.data.repository

import every.lol.com.core.domain.repository.SocialLoginRepository
import every.lol.com.core.network.datasource.SocialLoginDataSource

class SocialLoginRepositoryImpl(
    private val socialLoginDataSource: SocialLoginDataSource
) : SocialLoginRepository {
    override suspend fun loginWithKakao(): Result<String> {
        return socialLoginDataSource.loginWithKakao()
    }
}
