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
    val postTitle: String,
    val postCreatedAt: String,
    val userNickname: String,
    val userProfilePicture: String,
    val thumbnailFileUrl: String,
    val commentCounts: Int
)