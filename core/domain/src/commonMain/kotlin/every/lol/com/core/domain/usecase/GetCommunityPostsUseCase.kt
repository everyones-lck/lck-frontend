package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.PostList


class GetCommunityPostsUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postType: String, page: Int, size: Int): Result<PostList> =
        communityRepository.postList(postType, page, size)
}