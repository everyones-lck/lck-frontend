package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.UserInform
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
                println("로그: 서버 응답 성공! 토큰 저장 시작")
                local.saveUserId(kakaoUserId)
                local.saveToken(
                    dto.accessToken,
                    dto.refreshToken,
                    dto.accessTokenExpirationTime,
                    dto.refreshTokenExpirationTime
                )
                println("로그: 저장 완료! accessToken: ${dto.accessToken}")
            }

    override suspend fun signup(request: UserInform): Result<Unit> {
        val signupRequest = SignupRequest(
            profileImage = request.profileImage,
            signupUserData = SignupUserData(
                kakaoUserId = request.kakaoUserId,
                nickName = request.nickname,
                role = "ROLE_USER",
                tier = "bronze",
                // Todo: 서버 수정시 teamId List로 넘겨주기
                teamId = request.teamId.firstOrNull() ?: 0
            )
        )

        return remote.signup(request = signupRequest)
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
        remote.nickname(nickname).toResult()

    override suspend fun getValidAccessToken(): String? {
        val authData = local.getAuthData() ?: return null
        println("로그: 로컬에서 가져온 데이터 -> $authData") // 이게 null이면 1번 범인
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
            println("로그: AccessToken 만료됨. 리프레시 시도!")

            if (now >= refreshExpiry) {
                println("로그: RefreshToken까지 만료됨. 다시 로그인해라 자슥아!")
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
                println("로그: 토큰 갱신 성공!")
                newData.accessToken
            } else {
                println("로그: 리프레시 API 실패")
                null
            }
        } else {
            authData.accessToken
        }
    }

}