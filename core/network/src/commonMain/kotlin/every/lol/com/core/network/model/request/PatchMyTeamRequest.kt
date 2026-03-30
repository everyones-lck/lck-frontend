package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchMyTeamRequest(
    val teamIds: List<Int>
)