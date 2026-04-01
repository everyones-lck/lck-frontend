package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.MatchesDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.MatchCandidateResponse
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchPogCandidateResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.model.response.SetPogCandidateResponse
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
        httpClient.get("/matches/$matchId/rate")
    }.asApiResponse()

    override suspend fun getSetPogCandidate(matchId: Int): ApiResponse<SetPogCandidateResponse> = runCatching {
        httpClient.get("/votes/set-pog/candidates"){
            url{
                parameters.append("matchId", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getMatchPogCandidate(matchId: Int): ApiResponse<MatchPogCandidateResponse> = runCatching {
        httpClient.get("/votes/match-pog/candidates"){
            url{
                parameters.append("matchId", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getMatchCandidate(matchId: Int): ApiResponse<MatchCandidateResponse> = runCatching {
        httpClient.get("/votes/match/candidates"){
            url {
                parameters.append("matchId", matchId.toString())
            }
        }
    }.asApiResponse()

}