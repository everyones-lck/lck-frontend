package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.HomeDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.HomeAlertsResponse
import every.lol.com.core.network.model.response.HomeNewsResponse
import every.lol.com.core.network.model.response.HomeTodayMatchResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class HomeDataSourceImpl(
    private val httpClient: HttpClient
): HomeDataSource {

    override suspend fun todayMatchHome(): ApiResponse<HomeTodayMatchResponse> = runCatching {
        httpClient.get("/home/today")
    }.asApiResponse()

    override suspend fun newsHome(): ApiResponse<HomeNewsResponse> = runCatching {
        httpClient.get("/home/news")
    }.asApiResponse()

    override suspend fun alertsHome(): ApiResponse<HomeAlertsResponse> = runCatching {
        httpClient.get("/home/alerts")
    }.asApiResponse()

}