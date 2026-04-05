package every.lol.com.core.network.model.response.aboutlck.player

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKPlayerWinnigHistoryResponse(
    val seasonNames: List<String>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
