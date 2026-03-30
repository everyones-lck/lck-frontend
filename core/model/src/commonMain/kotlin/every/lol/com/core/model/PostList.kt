package every.lol.com.core.model


data class PostList(
    val postDetailList: List<PostListDetail>,
    val isLast: Boolean
)

data class PostListDetail(
    val postId: Int,
    val postTitle: String,
    val postCreatedAt: String,
    val userNickname: String,
    val userProfilePicture: String,
    val thumbnailFileUrl: String,
    val commentCounts: Int
)