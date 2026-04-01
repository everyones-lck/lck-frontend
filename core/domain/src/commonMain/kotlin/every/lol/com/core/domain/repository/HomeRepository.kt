package every.lol.com.core.domain.repository

import every.lol.com.core.model.HomeAlerts
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeRanking
import every.lol.com.core.model.HomeTodayMatch

interface HomeRepository {
    suspend fun todayMatchHome(): Result<HomeTodayMatch>
    suspend fun ranking(): Result<HomeRanking>
    suspend fun newsHome(): Result<HomeNews>
    suspend fun alertsHome(): Result<HomeAlerts>
}