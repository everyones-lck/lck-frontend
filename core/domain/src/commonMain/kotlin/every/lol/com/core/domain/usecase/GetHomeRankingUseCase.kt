package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.HomeRanking


class GetHomeRankingUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeRanking> =
        homeRepository.ranking()
}