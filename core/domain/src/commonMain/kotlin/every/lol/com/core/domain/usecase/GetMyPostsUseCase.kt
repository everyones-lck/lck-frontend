package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.Posts


class GetMyPostsUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(page: Int): Result<Posts> =
        myPagesRepository.getPosts(page)
}