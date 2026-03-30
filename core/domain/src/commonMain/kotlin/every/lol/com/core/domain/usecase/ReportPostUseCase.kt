package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository


class ReportPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit?> =
        communityRepository.reportPost(postId, "신고했으")
}