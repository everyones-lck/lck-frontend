package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository


class ReportCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(commentId: Int, reportDetail: String): Result<Unit?> =
        communityRepository.reportComment(commentId, reportDetail)
}