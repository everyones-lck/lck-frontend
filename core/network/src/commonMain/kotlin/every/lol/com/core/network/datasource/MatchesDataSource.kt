package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse

interface MatchesDataSource {
    suspend fun getMatches(): ApiResponse<MatchInfoResponse>
    suspend fun getMatchVoteRate(matchId: Long): ApiResponse<MatchVoteRateResponse>
}