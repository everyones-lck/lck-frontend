package every.lol.com.core.domain.repository

import every.lol.com.core.model.MatchCandidate
import every.lol.com.core.model.MatchInfo
import every.lol.com.core.model.MatchPogCandidate
import every.lol.com.core.model.MatchVoteRate
import every.lol.com.core.model.SetPogCandidate

interface MatchesRepository {
    suspend fun getMatches(): Result<MatchInfo>
    suspend fun getMatchVoteRate(matchId: Long): Result<MatchVoteRate>
    suspend fun getSetPogCandidate(matchId: Long): Result<SetPogCandidate>
    suspend fun getMatchPogCandidate(matchId: Long): Result<MatchPogCandidate>
    suspend fun getMatchCandidate(matchId: Long): Result<MatchCandidate>
}