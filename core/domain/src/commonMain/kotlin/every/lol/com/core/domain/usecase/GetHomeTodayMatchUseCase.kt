package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.HomeTodayMatch


class GetHomeTodayMatchUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeTodayMatch> =
        homeRepository.todayMatchHome()
}