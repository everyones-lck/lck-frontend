package every.lol.com.core.domain.repository

import every.lol.com.core.model.AboutLCKTeamHistory
import every.lol.com.core.model.Match

interface AboutLCKRepository {
    suspend fun match(searchData: String): Result<Match>
    suspend fun teamWinningHistory(teamId: Int, page: Int, size: Int): Result<AboutLCKTeamHistory>
}