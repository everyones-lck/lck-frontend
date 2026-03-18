package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.UserInform


class GetProfileUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(): Result<UserInform> {
        return myPagesRepository.getProfile()
    }
}