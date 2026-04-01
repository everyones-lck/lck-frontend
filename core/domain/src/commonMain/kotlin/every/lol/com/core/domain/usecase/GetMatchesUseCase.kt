package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchInfo

class GetMatchesUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(): Result<MatchInfo> =
        matchesRepository.getMatches()
}