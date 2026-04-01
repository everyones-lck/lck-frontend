package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SetPogResultResponse(
    val sets: List<SetPogResultSetResponse>
)

@Serializable
data class SetPogResultSetResponse(
    val setIndex: Int,
    val results: List<SetPogResultItemResponse>
)

@Serializable
data class SetPogResultItemResponse(
    val playerId: Long?,
    val playerName: String,
    val voteCount: Int,
    val voteRate: Double
)