package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.PostDetail


class GetReadPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Result<PostDetail> =
        communityRepository.detailPost(postId)
}