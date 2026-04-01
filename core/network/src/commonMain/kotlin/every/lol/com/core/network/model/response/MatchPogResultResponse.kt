package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MatchPogResultResponse(
    val matchId: Long,
    val results: List<MatchPogResultItemResponse>
)

@Serializable
data class MatchPogResultItemResponse(
    val playerId: Long?,
    val playerName: String,
    val voteCount: Int,
    val voteRate: Double
)