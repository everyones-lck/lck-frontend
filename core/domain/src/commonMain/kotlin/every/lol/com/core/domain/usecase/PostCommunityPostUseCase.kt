package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.model.MediaFile


class PostCommunityPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(files: List<MediaFile>?=null, type: String, title: String, content: String): Result<Unit> =
        communityRepository.postPost(files, type, title, content)
}