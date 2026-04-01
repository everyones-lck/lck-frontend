package every.lol.com.core.model

data class MatchPogCandidate(
    val matchId: Int,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidate>,
    val myVotedPlayerId: Int
)