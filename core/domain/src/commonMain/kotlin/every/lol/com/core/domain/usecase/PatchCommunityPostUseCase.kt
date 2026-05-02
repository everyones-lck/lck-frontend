package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.MediaFile
import every.lol.com.core.model.PostBlock


class PatchCommunityPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        postId: Int,
        newFiles: List<MediaFile>? = null,
        type: String,
        title: String,
        blocks: List<PostBlock>,
        platformContext: Any
    ): Result<Unit> =
        communityRepository.editPost(postId, newFiles, type, title, blocks, platformContext)
}