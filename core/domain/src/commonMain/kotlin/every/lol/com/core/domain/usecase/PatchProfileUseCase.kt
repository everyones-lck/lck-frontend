package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository


class PatchProfileUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(nickname: String, profileImage: ByteArray?): Result<Unit> =
        myPagesRepository.patchProfile(nickname, profileImage)
}