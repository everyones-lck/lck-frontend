package every.lol.com.core.model

data class SetPogResult(
    val sets: List<SetPogSetResult>
)

data class SetPogSetResult(
    val setIndex: Int,
    val results: List<SetPogVoteResult>
)

data class SetPogVoteResult(
    val playerId: Long?,
    val playerName: String,
    val voteCount: Int,
    val voteRate: Double
)