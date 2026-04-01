package every.lol.com.core.model

data class SetPogCandidate(
    val sets: List<SetPogCandidateDetail>
)

data class SetPogCandidateDetail(
    val setIndex: Int,
    val winnerTeamName: String,
    val candidates: List<PogCandidateCandidate>,
    val myVotedPlayerId: Long? = null
)

data class PogCandidateCandidate(
    val playerId: Int,
    val playerName: String
)
