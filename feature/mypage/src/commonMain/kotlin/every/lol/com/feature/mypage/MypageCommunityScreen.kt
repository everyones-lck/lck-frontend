package every.lol.com.feature.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.DefaultScreen
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.CommunityItem
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState
import everylol.feature.mypage.generated.resources.Res
import everylol.feature.mypage.generated.resources.ic_arrow_bottom
import everylol.feature.mypage.generated.resources.ic_arrow_up
import org.jetbrains.compose.resources.painterResource

@Composable
private fun MypageCommunityScreen(
    state: MypageUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onIntent: (MypageIntent) -> Unit
){
    val communityState = state as? MypageUiState.Community
    var expanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(MypageUiState.CommunityTab.POST) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "My Post / Comment")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 12.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = !expanded },
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(if (expanded) Res.drawable.ic_arrow_up else Res.drawable.ic_arrow_bottom),
                            contentDescription = null,
                            tint = EveryLoLTheme.color.grayScale200
                        )
                        Text(
                            text = if (selectedTab == MypageUiState.CommunityTab.POST) "My Post" else "My Comment",
                            style = EveryLoLTheme.typography.subtitle03,
                            color = EveryLoLTheme.color.grayScale200
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .clip(RoundedCornerShape(0.dp, 0.dp, 4.dp, 4.dp))
                            .background(EveryLoLTheme.color.grayScale900)
                            .padding(14.dp, 5.dp)
                    ) {
                        MypageUiState.CommunityTab.entries.filter { it != selectedTab }
                            .forEach { tab ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = if (tab == MypageUiState.CommunityTab.POST) "My Post" else "My Comment",
                                            style = EveryLoLTheme.typography.body03,
                                            color = EveryLoLTheme.color.grayScale200
                                        )
                                    },
                                    onClick = {
                                        selectedTab = tab
                                        expanded = false
                                        if (tab == MypageUiState.CommunityTab.POST) {
                                            onIntent(MypageIntent.FetchMyPosts)
                                        } else {
                                            onIntent(MypageIntent.FetchMyComments)
                                        }
                                    }
                                )
                            }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    communityState?.let { data ->
                        if (selectedTab == MypageUiState.CommunityTab.POST) {
                            if (data.posts.isEmpty()) {
                                DefaultScreen(
                                    title = "게시글이 아직 없습니다",
                                    description = "첫 게시글을 작성해보세요"
                                )
                            } else {
                                data.posts.forEach { post ->
                                    CommunityItem(
                                        type = MypageUiState.CommunityTab.POST,
                                        title = post.title,
                                        content = null, //Todo: API 수정 후 반영
                                        postType = post.postType
                                    )
                                }
                            }
                        } else {
                            if (data.posts.isEmpty()) {
                                DefaultScreen(
                                    title = "댓글이 아직 없습니다",
                                    description = "첫 댓글을 작성해보세요"
                                )
                            } else {
                                data.comments.forEach { comment ->
                                    CommunityItem(
                                        type = MypageUiState.CommunityTab.COMMENT,
                                        title = null, // TODO: API 수정 후 반영
                                        content = comment.content,
                                        postType = comment.postType
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}