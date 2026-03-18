package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository


class WithdrawalUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(): Result<Unit?> =
        myPagesRepository.withdrawal()
}