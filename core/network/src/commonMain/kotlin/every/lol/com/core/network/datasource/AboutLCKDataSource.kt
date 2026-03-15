package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.AboutLCKTeamHistoryResponse
import every.lol.com.core.network.model.response.MatchResponse

interface AboutLCKDataSource {
    suspend fun aboutLCKMatch(searchData:String): ApiResponse<MatchResponse>
    suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamHistoryResponse>
}