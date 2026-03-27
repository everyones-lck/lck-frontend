package every.lol.com.feature.community.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.CommentList
import every.lol.com.core.model.PostDetail
import every.lol.com.core.model.PostListDetail

@Immutable
sealed interface CommunityUiState {
    enum class CommunityTab(val displayName: String) {
        ALL("전체"),
        TALK("잡담"),
        QUESTION("질문"),
        TRADE("후기/거래"),
        HOT("인기")
    }

    data object Loading : CommunityUiState {
        val selectedTab = CommunityTab.ALL
    }

    data class Community(
        val selectedTab: CommunityTab = CommunityTab.ALL,
        val isLoading: Boolean = false,
        val popularPosts: List<PostDetail> = emptyList(),
        val posts: List<PostListDetail> = emptyList(),
    ) : CommunityUiState

    data class Read(
        val isLoading: Boolean = false,
        val postId: Int,
        val post: PostDetail? = null,
        val comments: List<CommentList> = emptyList(),
    ): CommunityUiState
}

sealed interface CommunityIntent{
    data object Loading : CommunityIntent
    data class ClickTab(val tab: CommunityUiState.CommunityTab) : CommunityIntent
    data object FetchPosts : CommunityIntent
    data class DetailPost(val postId: Int) : CommunityIntent

}