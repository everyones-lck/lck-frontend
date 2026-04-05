package every.lol.com.core.domain.repository

import every.lol.com.core.model.HomeAlerts
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeTodayMatch
import every.lol.com.core.model.Ranking

interface HomeRepository {
    suspend fun todayMatchHome(): Result<HomeTodayMatch>
    suspend fun ranking(): Result<Ranking>
    suspend fun newsHome(): Result<HomeNews>
    suspend fun alertsHome(): Result<HomeAlerts>
}