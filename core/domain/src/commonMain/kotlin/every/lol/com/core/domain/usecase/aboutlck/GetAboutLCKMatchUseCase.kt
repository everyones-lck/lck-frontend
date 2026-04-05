package every.lol.com.core.domain.usecase.aboutlck

import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.model.aboutlck.match.AboutLCKMatch


class GetAboutLCKMatchUseCase(
    private val aboutLCKRepository: AboutLCKRepository
) {
    suspend operator fun invoke(searchDate: String): Result<AboutLCKMatch?> =
        aboutLCKRepository.aboutLCKMatch(searchDate)
}