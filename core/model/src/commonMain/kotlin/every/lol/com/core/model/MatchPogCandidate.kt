package every.lol.com.core.model

data class MatchPogCandidate(
    val matchId: Long,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidate>,
    val myVotedPlayerId: Long? = null
)