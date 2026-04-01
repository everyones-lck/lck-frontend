package every.lol.com.core.model

data class MatchCandidate(
    val matchId: Long,
    val votable: Boolean,
    val team1: MatchCandidateTeam,
    val team2: MatchCandidateTeam,
    val myVotedTeamId: Long?=null
)

data class MatchCandidateTeam(
    val teamId: Int,
    val teamName: String
)