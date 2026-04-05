package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.aboutlck.match.AboutLCKMatchResponse
import every.lol.com.core.network.model.response.aboutlck.player.AboutLCKPlayerInformationResponse
import every.lol.com.core.network.model.response.aboutlck.player.AboutLCKPlayerTeamHistoryResponse
import every.lol.com.core.network.model.response.aboutlck.player.AboutLCKPlayerWinnigHistoryResponse
import every.lol.com.core.network.model.response.aboutlck.team.AboutLCKTeamPlayerHistoryResponse
import every.lol.com.core.network.model.response.aboutlck.team.AboutLCKTeamPlayerInformationResponse
import every.lol.com.core.network.model.response.aboutlck.team.AboutLCKTeamRatingHistoryResponse
import every.lol.com.core.network.model.response.aboutlck.team.AboutLCKTeamRatingResponse
import every.lol.com.core.network.model.response.aboutlck.team.AboutLCKTeamWinningHistoryResponse

interface AboutLCKDataSource {
    suspend fun aboutLCKMatch(searchData:String): ApiResponse<AboutLCKMatchResponse>
    suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamWinningHistoryResponse>
    suspend fun aboutLCKTeamRatingHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamRatingHistoryResponse>
    suspend fun aboutLCKTeamPlayerHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamPlayerHistoryResponse>
    suspend fun aboutLCKTeamPlayerInformation(teamId: Int, seasonName: String, playerRole: String): ApiResponse<AboutLCKTeamPlayerInformationResponse>
    suspend fun aboutLCKTeamRating(page: Int, size: Int): ApiResponse<AboutLCKTeamRatingResponse>
    suspend fun aboutLCKPlayerWinningHistory(playerId: Int, page: Int, size: Int): ApiResponse<AboutLCKPlayerWinnigHistoryResponse>
    suspend fun aboutLCKPlayerTeamHistory(playerId: Int, page: Int, size: Int): ApiResponse<AboutLCKPlayerTeamHistoryResponse>
    suspend fun aboutLCKPlayerInformation(playerId: Int): ApiResponse<AboutLCKPlayerInformationResponse>

}