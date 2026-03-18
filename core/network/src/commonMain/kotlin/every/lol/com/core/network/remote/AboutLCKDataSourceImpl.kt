package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.AboutLCKDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.AboutLCKTeamHistoryResponse
import every.lol.com.core.network.model.response.MatchResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class AboutLCKDataSourceImpl(
    private val httpClient: HttpClient
): AboutLCKDataSource {

    override suspend fun aboutLCKMatch(searchData: String): ApiResponse<MatchResponse> = runCatching {
        httpClient.get("/aboutlck/match"){
            url{
                parameters.append("searchData", searchData)
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/team/$teamId/winning-history"){
            url{
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()


}