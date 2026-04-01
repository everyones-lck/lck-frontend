package every.lol.com.core.domain.repository

import every.lol.com.core.model.MatchCandidate
import every.lol.com.core.model.MatchInfo
import every.lol.com.core.model.MatchPogResult
import every.lol.com.core.model.MatchPogCandidate
import every.lol.com.core.model.MatchVoteRate
import every.lol.com.core.model.SetPogResult
import every.lol.com.core.model.SetPogVoteItem
import every.lol.com.core.model.SetPogCandidate

interface MatchesRepository {
    suspend fun getMatches(): Result<MatchInfo>
    suspend fun getMatchVoteRate(matchId: Long): Result<MatchVoteRate>
    suspend fun postMatchVote(matchId: Long, teamId: Int): Result<Unit?>
    suspend fun postSetPogVote(matchId: Long, setPogVotes: List<SetPogVoteItem>): Result<Unit?>
    suspend fun postMatchPogVote(matchId: Long, playerId: Long?): Result<Unit?>
    suspend fun getSetPogResult(matchId: Long): Result<SetPogResult>
    suspend fun getMatchPogResult(matchId: Long): Result<MatchPogResult>
    suspend fun getSetPogCandidate(matchId: Long): Result<SetPogCandidate>
    suspend fun getMatchPogCandidate(matchId: Long): Result<MatchPogCandidate>
    suspend fun getMatchCandidate(matchId: Long): Result<MatchCandidate>
}