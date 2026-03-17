package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository

class CheckAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return runCatching {
            val token = authRepository.getValidAccessToken()
            println("로그: 토큰: $token")
            token?.isNotBlank() == true
        }
    }
}