package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.MatchCandidateResponse
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchPogCandidateResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.model.response.SetPogCandidateResponse

interface MatchesDataSource{
    suspend fun getMatches(): ApiResponse<MatchInfoResponse>
    suspend fun getMatchVoteRate(matchId: Long): ApiResponse<MatchVoteRateResponse>
    suspend fun getSetPogCandidate(matchId: Int): ApiResponse<SetPogCandidateResponse>
    suspend fun getMatchPogCandidate(matchId: Int): ApiResponse<MatchPogCandidateResponse>
    suspend fun getMatchCandidate(matchId: Int): ApiResponse<MatchCandidateResponse>
}