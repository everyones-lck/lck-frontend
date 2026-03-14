package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable


@Serializable
data class ReportPostRequest(
    val postId: Int,
    val reportDetail: String
)