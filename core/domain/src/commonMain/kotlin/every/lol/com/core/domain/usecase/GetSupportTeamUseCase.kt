package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.AuthRepository


class GetSupportTeamUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Int>> {
        return runCatching {
            val supportTeams = authRepository.getSupportTeam()
            if(supportTeams != null)   println("응원팀 불러오기 성공")
            supportTeams ?: emptyList()
        }
    }
}