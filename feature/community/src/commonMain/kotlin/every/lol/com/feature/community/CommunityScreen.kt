package every.lol.com.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.component.EverylolFab
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.CommunityItem
import every.lol.com.feature.community.component.PopularPost
import every.lol.com.feature.community.component.TabBar
import every.lol.com.feature.community.component.TitleText
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_no_content
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommunityRoute(
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit,
    onReadClick: (Int) -> Unit,
    onWriteClick: () -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityEvent.NavigateCommunityHome -> onBackClick
                is CommunityEvent.NavigateWrite -> {  }
                else -> {}
            }
        }
    }

    val communityState = uiState as? CommunityUiState.Community
    if (communityState == null) {
        Box(modifier = Modifier.fillMaxSize().everylolDefault(EveryLoLTheme.color.newBg, false), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = EveryLoLTheme.color.grayScale200)
        }
    } else {
        CommunityScreen(
            state = communityState,
            onReadClick = onReadClick,
            onWriteClick = onWriteClick,
            onIntent = viewModel::onIntent
        )
    }
}


@Composable
fun CommunityScreen(
    state: CommunityUiState,
    onReadClick: (Int) -> Unit,
    onWriteClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val communityState = state as? CommunityUiState.Community

    val currentTab = communityState?.selectedTab ?: CommunityUiState.CommunityTab.ALL
    val popularPosts = communityState?.popularPosts ?: emptyList()
    val posts = communityState?.posts ?: emptyList()

    val listState = rememberLazyListState()
    val isAllTab = currentTab == CommunityUiState.CommunityTab.ALL

    val shouldLoadMore = remember(listState, posts, currentTab) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) return@derivedStateOf false

            val lastVisibleItem = visibleItemsInfo.last()
            val lastVisibleItemIndex = lastVisibleItem.index
            val totalItemsCount = layoutInfo.totalItemsCount

            val threshold = if (isAllTab) 2 else 1

            posts.isNotEmpty() && lastVisibleItemIndex >= (totalItemsCount - threshold)
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && communityState?.isLoading == false) {
            onIntent(CommunityIntent.LoadNextPage)
        }
    }

    LaunchedEffect(currentTab) {
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(title = "커뮤니티")
            },
            floatingActionButton = {
                EverylolFab(
                    modifier = Modifier.padding(bottom = 107.dp),
                    onClick = {onWriteClick()}
                )
            }
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TabBar(
                            tabItems = CommunityUiState.CommunityTab.entries,
                            selectedTab = currentTab,
                            onTabSelected = { onIntent(CommunityIntent.ClickTab(it)) },
                            getDisplayName = { it.displayName }
                        )

                        if (currentTab == CommunityUiState.CommunityTab.ALL) {
                            TitleText("주간 인기글")
                            PopularPost(popularPosts = popularPosts)
                            TitleText("전체 게시판")
                        }
                    }
                }
                if (posts.isEmpty() && !communityState!!.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(284.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(EveryLoLTheme.color.grayScale1000),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_no_content),
                                    contentDescription = null,
                                    tint = EveryLoLTheme.color.community600
                                )
                                Text(
                                    text = "첫번째 게시글을 작성해보세요",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600
                                )
                            }
                        }
                    }
                } else {
                    items(items = posts, key = { it.postId }) { post ->
                        CommunityItem(
                            type = currentTab,
                            post = post,
                            onPostClick = { onReadClick(post.postId) }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}