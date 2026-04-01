package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository

class PostMatchPogVoteUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(
        matchId: Long,
        playerId: Long?
    ): Result<Unit?> =
        matchesRepository.postMatchPogVote(
            matchId = matchId,
            playerId = playerId
        )
}