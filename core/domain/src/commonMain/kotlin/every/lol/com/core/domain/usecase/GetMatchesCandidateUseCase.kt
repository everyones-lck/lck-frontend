package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.MatchCandidate

class GetMatchesCandidateUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<MatchCandidate> =
        matchesRepository.getMatchCandidate(matchId)
}