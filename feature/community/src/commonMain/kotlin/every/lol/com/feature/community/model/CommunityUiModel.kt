package every.lol.com.feature.community.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.CommentList
import every.lol.com.core.model.PopularPostListDetail
import every.lol.com.core.model.PostDetail
import every.lol.com.core.model.PostListDetail

@Immutable
sealed interface CommunityUiState {
    enum class CommunityTab(val displayName: String) {
        ALL("전체"),
        TALK("잡담"),
        QUESTION("질문"),
        TRADE("후기")
    }

    enum class WriteTab(val displayName: String) {
        TALK("잡담"),
        QUESTION("질문"),
        TRADE("후기")
    }

    data object Loading : CommunityUiState

    data class Community(
        val selectedTab: CommunityTab = CommunityTab.ALL,
        val isLoading: Boolean = false,
        val popularPosts: List<PopularPostListDetail> = emptyList(),
        val posts: List<PostListDetail> = emptyList(),
    ) : CommunityUiState

    data class Read(
        val isLoading: Boolean = false,
        val postId: Int,
        val post: PostDetail? = null,
        val isMine: Boolean = false,
        val comments: List<CommentList> = emptyList(),
        val isLiked: Boolean = false,
        val likeCount: Int = 0
    ) : CommunityUiState

    data class Write(
        val postId: Int? = null,
        val isLoading: Boolean = false,
        val selectedTab: WriteTab = WriteTab.TALK,
        val title: String = "",
        val content: String = "",
        val selectedMedias: List<MediaItem> = emptyList(),
    ) : CommunityUiState

    data class MediaItem(
        val id: String,
        val uriString: String,
        val isVideo: Boolean = false,
        val thumbnail: ByteArray? = null,
        val durationMs: Long = 0L,
        val order: Int = -1
    )
}

sealed interface CommunityIntent{
    data object Loading : CommunityIntent
    data class ClickTab(val tab: CommunityUiState.CommunityTab) : CommunityIntent
    data object LoadNextPage : CommunityIntent
    data class ClickWriteTab(val tab: CommunityUiState.WriteTab) : CommunityIntent
    data object FetchPosts : CommunityIntent
    data class DetailPost(val postId: Int, val isRefresh: Boolean = false) : CommunityIntent
    data class DeletePost(val postId: Int) : CommunityIntent
    data class DeleteComment(val commentId: Int) : CommunityIntent
    data class ReportComment(val commentId: Int, val reportDetail: String) : CommunityIntent
    data class ReportPost(val postId: Int, val reportDetail: String) : CommunityIntent
    data class ChangeTitle(val title: String) : CommunityIntent
    data class ChangeContent(val content: String) : CommunityIntent
    data class WritePost(
        val title: String,
        val content: String,
        val medias: List<CommunityUiState.MediaItem>
    ) : CommunityIntent

    data class LoadPostForEdit(val postId: Int) : CommunityIntent

    data class EditPost(
        val postId: Int,
        val title: String,
        val content: String,
        val medias: List<CommunityUiState.MediaItem>,
        val tab:CommunityUiState.WriteTab
    ) : CommunityIntent

    data object OpenGallery : CommunityIntent
    data class AddMedias(val medias: List<CommunityUiState.MediaItem>) : CommunityIntent
    data class RemoveMedia(val index: Int) : CommunityIntent
    data class MoveMedia(val from: Int, val to: Int) : CommunityIntent
    data class UpdateMediaOrder(val mediaId: String, val newOrder: Int) : CommunityIntent
    data class WriteComment(val postId: Int, val content: String) : CommunityIntent
    data class WriteReply(val postId: Int, val parentCommentId: Long, val content: String) : CommunityIntent
    data class ShowMessage(val message: String) : CommunityIntent
    data class LikePost(val postId: Int) : CommunityIntent
}