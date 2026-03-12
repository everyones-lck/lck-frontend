package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository

class RefreshUseCase(
    val authRepository: AuthRepository
) {
    suspend operator fun invoke(kakaoUserId: String): Result<Unit> {
        return authRepository.refresh(kakaoUserId)
    }
}