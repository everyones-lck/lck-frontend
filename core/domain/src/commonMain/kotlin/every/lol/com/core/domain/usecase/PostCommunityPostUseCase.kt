package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.MediaFile
import every.lol.com.core.model.PostBlock


class PostCommunityPostUseCase(
    private val communityRepository: CommunityRepository,
) {
    suspend operator fun invoke(
        files: List<MediaFile>? = null,
        type: String,
        title: String,
        blocks: List<PostBlock>,
        platformContext: Any
    ): Result<Unit> =
        communityRepository.postPost(files, type, title, blocks, platformContext)
}