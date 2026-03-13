package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.model.DomainException

class NicknameUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> =
        authRepository.nickname(nickname).mapCatching { isAvailable ->
            when (isAvailable) {
                true -> Unit
                false -> throw DomainException.DuplicateNicknameException()
                null -> throw IllegalStateException("서버 응답 데이터가 비어있습니다.")
            }
        }

}