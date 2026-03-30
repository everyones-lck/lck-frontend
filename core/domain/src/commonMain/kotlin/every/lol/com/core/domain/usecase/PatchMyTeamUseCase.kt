package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository


class PatchMyTeamUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(teamIds: List<Int>): Result<Unit> {
        return myPagesRepository.patchMyTeam(teamIds)
    }
}