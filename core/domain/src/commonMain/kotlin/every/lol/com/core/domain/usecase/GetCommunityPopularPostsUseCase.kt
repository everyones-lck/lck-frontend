package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.PopularPostList


class GetCommunityPopularPostsUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(period: String): Result<PopularPostList> =
        communityRepository.popularPostList(period)
}