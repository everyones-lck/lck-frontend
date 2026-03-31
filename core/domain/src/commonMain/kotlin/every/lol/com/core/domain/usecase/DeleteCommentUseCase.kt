package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository


class DeleteCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(commentId: Int): Result<Unit?> =
        communityRepository.deleteComment(commentId)
}