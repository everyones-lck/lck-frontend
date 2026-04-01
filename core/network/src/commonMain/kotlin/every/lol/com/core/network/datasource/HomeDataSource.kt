package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.HomeAlertsResponse
import every.lol.com.core.network.model.response.HomeNewsResponse
import every.lol.com.core.network.model.response.HomeTodayMatchResponse

interface HomeDataSource{
    suspend fun todayMatchHome(): ApiResponse<HomeTodayMatchResponse>
    suspend fun newsHome(): ApiResponse<HomeNewsResponse>
    suspend fun alertsHome(): ApiResponse<HomeAlertsResponse>
}