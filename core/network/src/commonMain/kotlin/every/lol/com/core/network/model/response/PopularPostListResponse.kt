package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PopularPostListResponse(
    val period: String,
    val postList: List<PopularPostListDetailResponse>,
)

@Serializable
data class PopularPostListDetailResponse(
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
    val commentCounts: Int,
    val likeCount: Int,
    val viewCount: Int
)