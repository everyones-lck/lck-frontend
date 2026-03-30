package every.lol.com.feature.mypage.model

import androidx.compose.runtime.Immutable
import every.lol.com.core.model.CommentsDetail
import every.lol.com.core.model.PostsDetail
import every.lol.com.core.model.Team

@Immutable
sealed interface MypageUiState {

    data class AppInform(
        val menuList: List<MypageMenu> = emptyList()
    ): MypageUiState
    data class Prediction(
        val rank: Int = 0,
        val predictions: List<PredictionItem> = emptyList(),
        val isLoading: Boolean = false
    ) : MypageUiState

    data class MVP(
        val mvpList: List<MVPItem> = emptyList(),
        val isLoading: Boolean = false
    ):MypageUiState

    data class MVPItem(
        val id: Int,
        val matchDate: String,
        val playerName: String,
        val playerTeam: Int
    )

    data class PredictionItem(
        val id: Int,
        val matchDate: String,
        val homeTeamId: Int,
        val awayTeamId: Int,
        val winnerTeamId: Int?,
        val myVoteTeamId: Int,
    )

    data class Community(
        val posts: List<PostsDetail> = emptyList(),
        val comments: List<CommentsDetail> = emptyList(),
        val selectedTab: CommunityTab = CommunityTab.POST,
        val isLoading: Boolean = false
    ): MypageUiState

    enum class CommunityTab {
        POST, COMMENT
    }
    data object Loading: MypageUiState
    data object Withdrawal: MypageUiState
    data class Mypage(
        val myInform: MyInform = MyInform(),
        val menuList: List<MypageMenu> = emptyList()
    ) : MypageUiState
    data class MyInform(
        val profileImage: String? = null,
        val nickName: String = "",
        val teamIds: Set<Team> = emptySet()
    )
    data class ProfileEdit(
        val originalNickname: String,
        val nickName: String = "",
        val originalProfileImage: Any?,
        val profileImage: Any?=null,
        val originalTeamId: Set<Team>,
        val teamId: Set<Team> = emptySet(),
        val isDuplicateChecked: Boolean = false,
        val isEnabled: Boolean = false,
        val isLoading: Boolean = false
    ): MypageUiState
    data class MypageMenu(
        val id: MypageMenuType,
        val title: String,
        val showDivider: Boolean = true
    )
    data class TosDetail(val id: Int): MypageUiState
    enum class MypageMenuType {
        PROFILE_EDIT, POST_COMMENT, POG_VOTE, PREDICTION, LOGOUT, WITHDRAWAL, APP_INFO,TOS_1,TOS_2,OPEN_SOURCE_LICENCE,APP_VERSION
    }
}

sealed interface MypageIntent{
    data object LoadInitial : MypageIntent
    data object LoadMypage : MypageIntent
    data object LoadAppInform: MypageIntent
    data object ClickBackToHome : MypageIntent
    data object LoadProfileEdit : MypageIntent
    data object LoadMVP: MypageIntent
    data object LoadPrediction: MypageIntent
    data class ClickMenu(val type: MypageUiState.MypageMenuType) : MypageIntent
    data object FetchMyPosts : MypageIntent
    data object FetchMyComments : MypageIntent
    data object SaveProfile: MypageIntent
    data class InputNickName(val nickName: String) : MypageIntent
    data class ClickCheckDuplicateNickname(val nickName: String) : MypageIntent
    data object Withdrawal : MypageIntent

}