package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository


class PostCommunityCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int, content: String): Result<Unit?> {
        return communityRepository.postComment(postId, content)
    }
}