package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.Ranking


class GetHomeRankingUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<Ranking> =
        homeRepository.ranking()
}