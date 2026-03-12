package every.lol.com.core.data.repository

import every.lol.com.core.common.toImageByteArray
import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.Signup
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.request.KakaoRequest
import every.lol.com.core.network.model.request.NicknameRequest
import every.lol.com.core.network.model.request.SignupRequest
import every.lol.com.core.network.model.request.SignupUserData

class AuthRepositoryImpl(
    private val remote: AuthDataSource,
    private val local: AuthLocalDataSource
): AuthRepository {

    override suspend fun login(kakaoUserId: String): Result<Unit> =
        remote.login(KakaoRequest(kakaoUserId))
            .toResult()
            .mapCatching { dto ->
                local.saveToken(
                    dto.accessToken,
                    dto.refreshToken,
                    dto.accessTokenExpirationTime,
                    dto.refreshTokenExpirationTime
                )
            }

    override suspend fun signup(request: Signup): Result<Unit> {
        val request = SignupRequest(
            profileImage =  request.profileImage.toImageByteArray(),
            signupUserData = SignupUserData(
                kakaoUserId = request.kakaoUserId,
                nickName = request.nickname,
                role = "ROLE_USER",
                tier = "bronze",
                // Todo: 서버 수정시 teamId List로 넘겨주기
                teamId = request.teamId.firstOrNull() ?: 0
            )
        )

        return remote.signup(request = request)
            .toResult()
            .mapCatching { dto ->
                local.saveToken(dto.accessToken, dto.refreshToken, dto.accessTokenExpirationTime, dto.refreshTokenExpirationTime)
            }
    }

    override suspend fun refresh(kakaoUserId: String): Result<Unit> =
        remote.refresh(request = KakaoRequest(kakaoUserId))
            .toResult()
            .mapCatching { dto ->
                local.saveToken(dto.accessToken, dto.refreshToken, dto.accessTokenExpirationTime, dto.refreshTokenExpirationTime)
            }

    override suspend fun nickname(nickname: String): Result<Boolean?> =
        remote.nickname(NicknameRequest(nickname))
            .toResult()

}