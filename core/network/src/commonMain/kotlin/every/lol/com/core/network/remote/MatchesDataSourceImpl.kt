package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.MatchesDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.MatchCandidateResponse
import every.lol.com.core.network.model.request.MatchPogVoteRequest
import every.lol.com.core.network.model.request.MatchVoteMakingRequest
import every.lol.com.core.network.model.request.SetPogVoteRequest
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchPogResultResponse
import every.lol.com.core.network.model.response.MatchPogCandidateResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.model.response.SetPogResultResponse
import every.lol.com.core.network.model.response.SetPogCandidateResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

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

    override suspend fun postMatchVote(request: MatchVoteMakingRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/votes/match/making") {
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun postSetPogVote(request: SetPogVoteRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/votes/pog/set") {
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun postMatchPogVote(request: MatchPogVoteRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/votes/pog/match") {
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun getSetPogResult(matchId: Long): ApiResponse<SetPogResultResponse> = runCatching {
        httpClient.get("/votes/set-pog/result") {
            url {
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getMatchPogResult(matchId: Long): ApiResponse<MatchPogResultResponse> = runCatching {
        httpClient.get("/votes/match-pog/result") {
            url {
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getSetPogCandidate(matchId: Long): ApiResponse<SetPogCandidateResponse> = runCatching {
        httpClient.get("/votes/set-pog/candidates"){
            url{
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getMatchPogCandidate(matchId: Long): ApiResponse<MatchPogCandidateResponse> = runCatching {
        httpClient.get("/votes/match-pog/candidates"){
            url{
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getMatchCandidate(matchId: Long): ApiResponse<MatchCandidateResponse> = runCatching {
        httpClient.get("/votes/match/candidates"){
            url {
                parameters.append("match-id", matchId.toString())
            }
        }
    }.asApiResponse()

}