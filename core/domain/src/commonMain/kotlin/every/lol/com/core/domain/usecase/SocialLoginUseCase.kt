package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.SocialLoginRepository
import every.lol.com.core.model.DomainException

class SocialLoginUseCase(
    private val socialLoginRepository: SocialLoginRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val kakaoResult = socialLoginRepository.loginWithKakao()
        val kakaoToken = kakaoResult.getOrNull()

        if (kakaoToken == null) {
            return Result.failure(kakaoResult.exceptionOrNull() ?: DomainException.UnknownException())
        }

        return authRepository.login(kakaoToken).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { error ->
                if (error is DomainException.InvalidJwtTokenException) {
                    Result.failure(
                        DomainException.UserNotRegisteredException(
                            kakaoUserId = kakaoToken,
                            cause = error
                        )
                    )
                } else {
                    Result.failure(error)
                }
            }
        )
    }
}