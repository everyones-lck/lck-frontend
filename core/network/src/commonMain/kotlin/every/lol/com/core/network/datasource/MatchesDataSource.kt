package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.MatchPogVoteRequest
import every.lol.com.core.network.model.request.MatchVoteMakingRequest
import every.lol.com.core.network.model.request.SetPogVoteRequest
import every.lol.com.core.network.model.response.MatchCandidateResponse
import every.lol.com.core.network.model.response.MatchInfoResponse
import every.lol.com.core.network.model.response.MatchPogResultResponse
import every.lol.com.core.network.model.response.MatchPogCandidateResponse
import every.lol.com.core.network.model.response.MatchVoteRateResponse
import every.lol.com.core.network.model.response.SetPogResultResponse
import every.lol.com.core.network.model.response.SetPogCandidateResponse

interface MatchesDataSource {
    suspend fun getMatches(): ApiResponse<MatchInfoResponse>
    suspend fun getMatchVoteRate(matchId: Long): ApiResponse<MatchVoteRateResponse>
    suspend fun postMatchVote(request: MatchVoteMakingRequest): ApiResponse<Unit?>
    suspend fun postSetPogVote(request: SetPogVoteRequest): ApiResponse<Unit?>
    suspend fun postMatchPogVote(request: MatchPogVoteRequest): ApiResponse<Unit?>
    suspend fun getSetPogResult(matchId: Long): ApiResponse<SetPogResultResponse>
    suspend fun getMatchPogResult(matchId: Long): ApiResponse<MatchPogResultResponse>
    suspend fun getSetPogCandidate(matchId: Long): ApiResponse<SetPogCandidateResponse>
    suspend fun getMatchPogCandidate(matchId: Long): ApiResponse<MatchPogCandidateResponse>
    suspend fun getMatchCandidate(matchId: Long): ApiResponse<MatchCandidateResponse>
}