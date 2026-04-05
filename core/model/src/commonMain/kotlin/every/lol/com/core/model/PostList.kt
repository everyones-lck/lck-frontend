package every.lol.com.core.model


data class PostList(
    val postDetailList: List<PostListDetail>,
    val isLast: Boolean
)

data class PostListDetail(
    val postId: Int,
    val postType: String,
    val postTitle: String,
    val postContent: String,
    val postCreatedAt: String,
    val userNickname: String,
    val userProfileUrl: String,
    val supportTeamNames: List<String>,
    val imageThumbnailUrl: String?=null,
    val videoThumbnailUrl: String?=null,
    val imageCounts: Int,
    val videoCounts: Int,
    val commentCounts: Int,
    val viewCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isWriter: Boolean
)