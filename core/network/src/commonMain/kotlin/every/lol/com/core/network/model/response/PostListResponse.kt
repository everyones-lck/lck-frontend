package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostListResponse(
    val postDetailList: List<PostListDetailResponse>,
    val isLast: Boolean
)

@Serializable
data class PostListDetailResponse(
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