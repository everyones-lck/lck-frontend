package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository


class DeletePostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit?> =
        communityRepository.deletePost(postId)
}