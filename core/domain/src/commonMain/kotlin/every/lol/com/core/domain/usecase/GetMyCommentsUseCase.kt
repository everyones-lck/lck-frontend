package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.Comments


class GetMyCommentsUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(page: Int): Result<Comments> =
        myPagesRepository.getComments(page)
}