package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.DomainException

class NicknameUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> =
        authRepository.nickname(nickname).mapCatching { isAvailable ->
            if (isAvailable == true) {
                Unit
            } else {
                throw DomainException.DuplicateNicknameException()
            }
        }

}