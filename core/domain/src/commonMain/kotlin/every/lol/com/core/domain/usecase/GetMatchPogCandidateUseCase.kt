package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchPogCandidate

class GetMatchPogCandidateUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<MatchPogCandidate> =
        matchesRepository.getMatchPogCandidate(matchId)
}