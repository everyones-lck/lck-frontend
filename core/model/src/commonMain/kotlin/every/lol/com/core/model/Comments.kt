package every.lol.com.core.model

data class Comments(
    val comments: List<CommentsDetail>,
    val isLast: Boolean
)

data class CommentsDetail(
    val commentId: Int,
    val postId: Int,
    val content: String,
    val postType: String
)