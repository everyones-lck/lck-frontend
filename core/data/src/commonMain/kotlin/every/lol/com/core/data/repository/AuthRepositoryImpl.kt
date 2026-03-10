package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.request.LoginRequest

class AuthRepositoryImpl(
    private val remote: AuthDataSource,
    private val local: AuthLocalDataSource
): AuthRepository {

    override suspend fun login(kakaoUserId: String): Result<Unit>{
        val request = LoginRequest(kakaoUserId)

        return remote.login(request = request)
            .toResult()
            .mapCatching { dto ->
                local.saveToken(dto.accessToken, dto.refreshToken, dto.accessTokenExpirationTime, dto.refreshTokenExpirationTime)
            }
    }
}