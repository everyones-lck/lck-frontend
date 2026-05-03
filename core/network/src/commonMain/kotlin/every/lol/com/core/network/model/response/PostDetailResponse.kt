package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostDetailResponse(
    val postId: Int,
    val postType: String,
    val writerProfileUrl: String,
    val writerNickname: String,
    val writerTeams: List<String>,
    val postTitle: String,
    val postCreatedAt: String,
    val isModified: Boolean,
    val isWriter: Boolean,
    val isLiked: Boolean,
    val likeCount: Int,
    val viewCount: Int,
    val commentCount: Int,
    val imageCounts: Int,
    val videoCounts: Int,
    val blocks: List<PostDetailBlocksResponse>,
    val commentList: List<CommentListResponse>?=null
)

@Serializable
data class PostDetailBlocksResponse(
    val sequence: Int,
    val type: String,
    val content: String?=null,
    val fileUrl: String?=null,
    val fileName: String?=null,
    val thumbnailUrl: String?=null
)

@Serializable
data class CommentListResponse(
    val commentId: Int,
    val parentCommentId: Int?=null,
    val profileImageUrl: String,
    val nickname: String,
    val supportTeams: List<String>,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val isWriter: Boolean,
    val replies: List<CommentRepliesListResponse>?=null
)

@Serializable
data class CommentRepliesListResponse(
    val commentId: Int,
    val parentCommentId: Int?=null,
    val profileImageUrl: String,
    val nickname: String,
    val supportTeams: List<String>,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val isWriter: Boolean,
)