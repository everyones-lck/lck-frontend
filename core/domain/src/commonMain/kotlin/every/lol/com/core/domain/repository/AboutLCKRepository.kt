package every.lol.com.core.domain.repository

import every.lol.com.core.model.aboutlck.match.AboutLCKMatch
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerInformation
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerTeamHistory
import every.lol.com.core.model.aboutlck.player.AboutLCKPlayerWinningHistory
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamPlayerHistory
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamPlayerInformation
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamRating
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamRatingHistory
import every.lol.com.core.model.aboutlck.team.AboutLCKTeamWinnigHistory

interface AboutLCKRepository {
    suspend fun aboutLCKMatch(searchDate: String): Result<AboutLCKMatch?>
    suspend fun aboutLCKTeamWinningHistory(teamId: Int, page: Int, size: Int):Result<AboutLCKTeamWinnigHistory>
    suspend fun aboutLCKTeamRatingHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamRatingHistory>
    suspend fun aboutLCKTeamPlayerHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamPlayerHistory>
    suspend fun aboutLCKTeamPlayerInformation(teamId: Int, seasonName: String, playerRole: String):Result<AboutLCKTeamPlayerInformation>
    suspend fun aboutLCKTeamRating(page: Int, size: Int):Result<AboutLCKTeamRating>
    suspend fun aboutLCKPlayerWinningHistory(playerId: Int, page: Int, size: Int): Result<AboutLCKPlayerWinningHistory>
    suspend fun aboutLCKPlayerTeamHistory(playerId: Int, page: Int, size: Int):Result<AboutLCKPlayerTeamHistory>
    suspend fun aboutLCKPlayerInformation(playerId: Int):Result<AboutLCKPlayerInformation>

}