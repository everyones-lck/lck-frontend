package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.AboutLCKDataSource
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
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class AboutLCKDataSourceImpl(
    private val httpClient: HttpClient
): AboutLCKDataSource {

    override suspend fun aboutLCKMatch(searchData: String): ApiResponse<AboutLCKMatchResponse> = runCatching {
        httpClient.get("/aboutlck/match"){
            url{
                parameters.append("searchData", searchData)
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamWinningHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/team/$teamId/winning-history"){
            url{
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamRatingHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamRatingHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/team/$teamId/rating-history"){
            url{
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamPlayerHistory(teamId: Int, page: Int, size: Int): ApiResponse<AboutLCKTeamPlayerHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/team/$teamId/player-history"){
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamPlayerInformation(teamId: Int, seasonName: String, playerRole: String): ApiResponse<AboutLCKTeamPlayerInformationResponse> = runCatching {
        httpClient.get("/aboutlck/team/$teamId/player-information"){
            url{
                parameters.append("seasonName", seasonName)
                parameters.append("playerRole", playerRole)
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKTeamRating(page: Int, size: Int): ApiResponse<AboutLCKTeamRatingResponse> = runCatching {
        httpClient.get("/aboutlck/team/rating") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKPlayerWinningHistory(playerId: Int, page: Int, size: Int): ApiResponse<AboutLCKPlayerWinnigHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/player/$playerId/winning-history") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKPlayerTeamHistory(playerId: Int, page: Int, size: Int): ApiResponse<AboutLCKPlayerTeamHistoryResponse> = runCatching {
        httpClient.get("/aboutlck/player/$playerId/team-history") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun aboutLCKPlayerInformation(playerId: Int): ApiResponse<AboutLCKPlayerInformationResponse> = runCatching {
        httpClient.get("/aboutlck/player/$playerId/information")
    }.asApiResponse()
}