package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.Signup
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.KakaoRequest
import every.lol.com.core.network.model.request.SignupRequest
import every.lol.com.core.network.model.request.SignupUserData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Clock

class AuthRepositoryImpl(
    private val remote: AuthDataSource,
    private val local: AuthLocalDataSource
): AuthRepository {

    override suspend fun login(kakaoUserId: String): Result<Unit> =
        remote.login(KakaoRequest(kakaoUserId))
            .toResult()
            .mapCatching { dto ->
                local.saveUserId(kakaoUserId)
                local.saveToken(
                    dto.accessToken,
                    dto.refreshToken,
                    dto.accessTokenExpirationTime,
                    dto.refreshTokenExpirationTime
                )
            }

    override suspend fun signup(request: Signup): Result<Unit> {
        val signupRequest = SignupRequest(
            profileImage = request.profileImage,
            signupUserData = SignupUserData(
                kakaoUserId = request.kakaoUserId,
                nickName = request.nickname,
                role = "ROLE_USER",
                tier = "bronze",
                teamIds = request.teamIds
            )
        )

        return remote.signup(signupRequest).toResult()
            .mapCatching { dto ->
                local.saveSupportTeam(request.teamIds)
                local.saveUserId(request.kakaoUserId)
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
        remote.nickname(nickname).toResult()

    override suspend fun getValidAccessToken(): String? {
        val authData = local.getAuthData() ?: return null
        val kakaoUserId = authData.kakaoUserId ?: return null

        val now = Clock.System.now().toEpochMilliseconds()
        fun parseServerTime(timeStr: String): Long {
            return try {
                val formatted = timeStr.replace(" ", "T")
                LocalDateTime.parse(formatted)
                    .toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            } catch (e: Exception) {
                0L
            }
        }
        val expiry = parseServerTime(authData.accessTokenExpirationTime)
        val refreshExpiry = parseServerTime(authData.refreshTokenExpirationTime)

        return if (now >= expiry) {

            if (now >= refreshExpiry) {
                local.clearAuthData()
                return null
            }
            val response = remote.refresh(KakaoRequest(kakaoUserId))
            if (response is ApiResponse.Success) {
                val newData = response.data
                local.saveToken(
                    newData.accessToken,
                    newData.refreshToken,
                    newData.accessTokenExpirationTime,
                    newData.refreshTokenExpirationTime
                )
                newData.accessToken
            } else {
                null
            }
        } else {
            authData.accessToken
        }
    }

    override suspend fun getSupportTeam(): List<Int>? {
        val supportTeams = local.getSupportTeamIdsOnce() ?: return null
        return supportTeams
    }
}