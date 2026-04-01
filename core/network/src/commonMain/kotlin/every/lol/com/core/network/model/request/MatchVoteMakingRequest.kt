package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MatchVoteMakingRequest(
    val matchId: Long,
    val teamId: Int
)