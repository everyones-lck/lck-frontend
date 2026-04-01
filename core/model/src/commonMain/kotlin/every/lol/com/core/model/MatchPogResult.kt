package every.lol.com.core.model

data class MatchPogResult(
    val matchId: Long,
    val results: List<MatchPogVoteResult>
)

data class MatchPogVoteResult(
    val playerId: Long?,
    val playerName: String,
    val voteCount: Int,
    val voteRate: Double
)