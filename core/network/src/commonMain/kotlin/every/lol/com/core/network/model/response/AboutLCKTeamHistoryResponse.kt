package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamHistoryResponse(
    val seasonNameList: List<String>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)