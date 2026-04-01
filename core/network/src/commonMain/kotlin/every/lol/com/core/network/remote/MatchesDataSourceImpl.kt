package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.MatchesDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class MatchesDataSourceImpl(
    private val httpClient: HttpClient
): MatchesDataSource {
    override suspend fun getMatches(): ApiResponse<MatchInfoResponse> = runCatching {
        httpClient.get("/home/today")
    }.asApiResponse()

    override suspend fun getMatchVoteRate(matchId: Long): ApiResponse<MatchVoteRateResponse> = runCatching {
        httpClient.get("/votes/match/rate") {
            url {
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

}