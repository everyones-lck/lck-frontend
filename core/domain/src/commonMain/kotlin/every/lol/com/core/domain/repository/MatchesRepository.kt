package every.lol.com.core.domain.repository

import every.lol.com.core.model.MatchInfo
import every.lol.com.core.model.MatchVoteRate

interface MatchesRepository {
    suspend fun getMatches(): Result<MatchInfo>
    suspend fun getMatchVoteRate(matchId: Long): Result<MatchVoteRate>
}