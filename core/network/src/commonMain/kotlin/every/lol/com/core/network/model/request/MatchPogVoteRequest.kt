package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MatchPogVoteRequest(
    val matchId: Long,
    val playerId: Long?
)