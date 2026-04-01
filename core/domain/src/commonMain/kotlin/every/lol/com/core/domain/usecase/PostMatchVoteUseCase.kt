package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository

class PostMatchVoteUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long, teamId: Int): Result<Unit?> =
        matchesRepository.postMatchVote(matchId, teamId)
}