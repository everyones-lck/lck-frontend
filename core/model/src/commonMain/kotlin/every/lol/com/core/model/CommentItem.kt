package every.lol.com.core.model

interface CommentItem {
    val commentId: Int
    val nickname: String
    val content: String
    val profileImageUrl: String
    val createdAt: String
    val isWriter: Boolean
    val isDeleted: Boolean
}