package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchCandidateResponse(
    val matchId: Int,
    val votable: Boolean,
    val team1: MatchCandidateTeamResponse,
    val team2: MatchCandidateTeamResponse,
    val myVotedTeamId: Int
)

@Serializable
data class MatchCandidateTeamResponse(
    val teamId: Int,
    val teamName: String
)