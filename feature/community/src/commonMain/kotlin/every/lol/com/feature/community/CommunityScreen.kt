package every.lol.com.feature.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.CommunityItem
import every.lol.com.feature.community.component.PopularPost
import every.lol.com.feature.community.component.TabBar
import every.lol.com.feature.community.component.TitleText
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import moe.tlaster.precompose.koin.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommunityRoute(
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit,
    onWriteSuccess: () -> Unit,
    onDeleteSuccess: () -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(CommunityIntent.Loading)
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                CommunityEvent.NavigateWrite -> onWriteSuccess()
                else -> {}
            }
        }
    }

    when (uiState) {
        is CommunityUiState.Community -> {
            CommunityScreen(
                state = uiState,
                onBackClick = onBackClick,
                onIntent = viewModel::onIntent
            )
        }
        else -> {}
    }
}


@Composable
fun CommunityScreen(
    state: CommunityUiState,
    onBackClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val communityState = state as? CommunityUiState.Community
    val popularPosts = communityState?.popularPosts ?: emptyList()
    val posts = communityState?.posts ?: emptyList()

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
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TabBar(
                    tabItems = CommunityUiState.CommunityTab.entries,
                    selectedTab = if (state is CommunityUiState.Community) state.selectedTab else CommunityUiState.CommunityTab.ALL,
                    onTabSelected = { tab ->
                        onIntent(CommunityIntent.ClickTab(tab))
                    }
                )
                if(state.selectedTab == CommunityUiState.CommunityTab.ALL) {
                    TitleText("주간 인기글")
                    PopularPost(popularPosts =popularPosts)
                    TitleText("전체 게시판")
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    if (posts.isEmpty()) {

                    } else {
                        posts.forEach { post ->
                            CommunityItem(
                                type = state.selectedTab,
                                post = post
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}