package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SetPogVoteRequest(
    val matchId: Long,
    val setPogVotes: List<SetPogVoteItemRequest>
)

@Serializable
data class SetPogVoteItemRequest(
    val setIndex: Int,
    val playerId: Long?
)