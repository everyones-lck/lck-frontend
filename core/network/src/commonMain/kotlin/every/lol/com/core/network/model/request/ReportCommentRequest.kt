package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable


@Serializable
data class ReportCommentRequest(
    val commentId: Int,
    val reportDetail: String
)