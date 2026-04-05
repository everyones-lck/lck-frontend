package every.lol.com.core.model


data class PopularPostList(
    val period: String,
    val postList: List<PopularPostListDetail>
)

data class PopularPostListDetail(
    val postId: Int,
    val postTypeName: String,
    val postTitle: String,
    val postContent: String,
    val postCreatedAt: String,
    val userNickname: String,
    val userProfilePicture: String,
    val imageThumbnailUrl: String?=null,
    val videoThumbnailUrl: String?=null,
    val imageCounts: Int,
    val videoCounts: Int,
    val commentCount: Int,
    val likeCount: Int,
    val viewCount: Int
)