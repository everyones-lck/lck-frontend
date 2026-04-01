package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.SetPogCandidate

class GetSetPogCandidateUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<SetPogCandidate> =
        matchesRepository.getSetPogCandidate(matchId)
}