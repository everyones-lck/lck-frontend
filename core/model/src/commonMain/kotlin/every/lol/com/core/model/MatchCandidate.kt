package every.lol.com.core.model

data class MatchCandidate(
    val matchId: Int,
    val votable: Boolean,
    val team1: MatchCandidateTeam,
    val team2: MatchCandidateTeam,
    val myVotedTeamId: Int
)

data class MatchCandidateTeam(
    val teamId: Int,
    val teamName: String
)