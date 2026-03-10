package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository

class LoginUseCase(
    val authRepository: AuthRepository
) {
    suspend operator fun invoke(kakaoUserId: String): Result<Unit> {
        /*val result = authRepository.login(kakaoUserId)
        result.onSuccess {

        }.onFailure {
            it.printStackTrace()
        }

        result.getOrThrow()*/
        return authRepository.login(kakaoUserId)
    }
}