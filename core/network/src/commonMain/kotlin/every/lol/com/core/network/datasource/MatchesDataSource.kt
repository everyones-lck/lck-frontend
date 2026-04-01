package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.MatchPogVoteRequest
import every.lol.com.core.network.model.request.MatchVoteMakingRequest
import every.lol.com.core.network.model.request.SetPogVoteRequest
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchPogResultResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.model.response.SetPogResultResponse

interface MatchesDataSource {
    suspend fun getMatches(): ApiResponse<MatchInfoResponse>
    suspend fun getMatchVoteRate(matchId: Long): ApiResponse<MatchVoteRateResponse>
    suspend fun postMatchVote(request: MatchVoteMakingRequest): ApiResponse<Unit?>
    suspend fun postSetPogVote(request: SetPogVoteRequest): ApiResponse<Unit?>
    suspend fun postMatchPogVote(request: MatchPogVoteRequest): ApiResponse<Unit?>
    suspend fun getSetPogResult(matchId: Long): ApiResponse<SetPogResultResponse>
    suspend fun getMatchPogResult(matchId: Long): ApiResponse<MatchPogResultResponse>
}