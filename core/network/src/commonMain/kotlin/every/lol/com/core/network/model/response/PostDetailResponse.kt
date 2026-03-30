package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostDetailResponse(
    val postType: String,
    val writerProfileUrl: String,
    val writerNickname: String,
    val postTitle: String,
    val postCreatedAt: String,
    val content: String,
    val isWriter: Boolean,
    val fileList: List<FileListResponse>,
    val commentList: List<CommentListResponse>
)

@Serializable
data class FileListResponse(
    val fileUrl: String,
    val isImage: Boolean
)

@Serializable
data class CommentListResponse(
    val profileImageUrl: String,
    val nickname: String,
    val content: String,
    val createdAt: String,
    val commentId: Int,
    val isWriter: Boolean
)