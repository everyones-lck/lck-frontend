package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class CommentsResponse(
    val comments: List<CommentsDetailResponse>,
    val isLast: Boolean
)

@Serializable
data class CommentsDetailResponse(
    val commentId: Int,
    val postId: Int,
    val content: String,
    val postType: String
)