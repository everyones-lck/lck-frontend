package every.lol.com.core.model

data class PostDetail(
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
    val blocks: List<PostDetailBlocks>,
    val commentList: List<CommentList>?=null
)

data class PostDetailBlocks(
    val sequence: Int,
    val type: String,
    val content: String?=null,
    val fileUrl: String?=null,
    val fileName: String?=null
)

data class CommentList(
    val commentId: Int,
    val parentCommentId: Int?=null,
    val profileImageUrl: String,
    val nickname: String,
    val supportTeams: List<String>,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val isWriter: Boolean,
    val replies: List<CommentRepliesList>?=null
)

data class CommentRepliesList(
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