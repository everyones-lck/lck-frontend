package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchVoteRate

class GetMatchVoteRateUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<MatchVoteRate> =
        matchesRepository.getMatchVoteRate(matchId)
}