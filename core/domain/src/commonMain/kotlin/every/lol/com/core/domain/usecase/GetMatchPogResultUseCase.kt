package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchPogResult

class GetMatchPogResultUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<MatchPogResult> =
        matchesRepository.getMatchPogResult(matchId)
}