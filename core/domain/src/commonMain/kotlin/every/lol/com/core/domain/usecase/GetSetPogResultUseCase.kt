package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.model.SetPogResult

class GetSetPogResultUseCase(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(matchId: Long): Result<SetPogResult> =
        matchesRepository.getSetPogResult(matchId)
}