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
    override val commentId: Int,
    val parentCommentId: Int?=null,
    override val profileImageUrl: String,
    override val nickname: String,
    val supportTeams: List<String>,
    override val content: String,
    override val createdAt: String,
    override val isDeleted: Boolean,
    override val isWriter: Boolean,
    val replies: List<CommentRepliesList>?=null
): CommentItem

data class CommentRepliesList(
    override val commentId: Int,
    val parentCommentId: Int?=null,
    override val profileImageUrl: String,
    override val nickname: String,
    val supportTeams: List<String>,
    override val content: String,
    override val createdAt: String,
    override val isDeleted: Boolean,
    override val isWriter: Boolean,
): CommentItem