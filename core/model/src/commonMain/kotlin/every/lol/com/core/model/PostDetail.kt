package every.lol.com.core.model

import kotlinx.serialization.Serializable


data class PostDetail(
    val postType: String,
    val writerProfileUrl: String,
    val writerNickName: String,
    val writerTeam: String,
    val postTitle: String,
    val postCreatedAt: String,
    val content: String,
    val isWriter: Boolean,
    val fileList: List<FileList>,
    val commentList: List<CommentList>
)

data class FileList(
    val fileUrl: String,
    val isImage: Boolean
)

data class CommentList(
    val profileImageUrl: String,
    val nickname: String,
    val supportTeam: String,
    val content: String,
    val createdAt: String,
    val commentId: Int,
    val isWriter: Boolean
)