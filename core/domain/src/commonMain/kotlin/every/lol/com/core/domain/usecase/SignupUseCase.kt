package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.Signup


class SignupUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(signupData: Signup): Result<Unit> {
        return authRepository.signup(signupData)
    }
}