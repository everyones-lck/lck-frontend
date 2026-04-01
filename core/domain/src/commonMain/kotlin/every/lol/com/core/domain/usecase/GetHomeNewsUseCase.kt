package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.HomeNews


class GetHomeNewsUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeNews> =
        homeRepository.newsHome()
}