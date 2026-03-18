package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository

class CheckAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return runCatching {
            val token = authRepository.getValidAccessToken()
            if(token != null)   println("토큰 불러오기 성공")
            token?.isNotBlank() == true
        }
    }
}