package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.model.HomeAlerts


class GetHomeAlertsUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeAlerts> =
        homeRepository.alertsHome()
}