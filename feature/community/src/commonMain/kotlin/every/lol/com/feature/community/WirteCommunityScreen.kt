package every.lol.com.feature.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTextField
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.TabBar
import every.lol.com.feature.community.component.TitleText
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import moe.tlaster.precompose.koin.koinViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WriteRoute(
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(CommunityIntent.ClickWriteTab(CommunityUiState.WriteTab.TALK))
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(CommunityIntent.Loading)
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityEvent.WriteSuccess -> onBackClick()
                else -> {}
            }
        }
    }

    val currentState = uiState
    if (currentState is CommunityUiState.Write) {
        WriteCommunityScreen(
            state = currentState,
            onBackClick = onBackClick,
            onIntent = viewModel::onIntent
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize().everylolDefault(EveryLoLTheme.color.newBg, false),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = EveryLoLTheme.color.grayScale200)
        }
    }
}

@Composable
fun WriteCommunityScreen(
    state: CommunityUiState.Write,
    onBackClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val isFormValid = state.title.isNotBlank() && state.content.isNotBlank()
    var showWritePostModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "글 작성하기")
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = 0.dp
                    )
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TitleText("카테고리")
                        TabBar(
                            tabItems = CommunityUiState.WriteTab.entries,
                            selectedTab = state.selectedTab,
                            onTabSelected = { onIntent(CommunityIntent.ClickWriteTab(it)) },
                            getDisplayName = { it.displayName }
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TitleText("제목")
                        EverylolTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.title,
                            onValueChange = { onIntent(CommunityIntent.ChangeTitle(it)) },
                            hint = "공백 포함 최대 50자 작성 가능합니다",
                            maxLine = 1,
                            community = true
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TitleText("내용")
                        EverylolTextField(
                            modifier = Modifier.fillMaxWidth().heightIn(min = 400.dp),
                            value = state.content,
                            onValueChange = { onIntent(CommunityIntent.ChangeContent(it))},
                            hint = "- 공백 포함 최대 2000자 작성가능합니다\n" +
                                    "- 과도한 비방 및 욕설이 포함된 게시물은 신고에 의해 무통보 삭제될 수 있습니다\n" +
                                    "- 초상권, 저작권 침해 및 기타 위법한 게시물은 관리자에 의해 무통보 삭제될 수 있습니다.\n" +
                                    "- 사진은 최대 10장, 영상은 최대 2개 까지 첨부가능합니다",
                            maxLine = 500,
                            community = true
                        )
                    }
                }
            }
            EverylolButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                text = "게시글 올리기",
                enabled = isFormValid,
                onClick = { showWritePostModal = true }
            )
            if(showWritePostModal) {
                EverylolModal(
                    title = "게시글이 완성되었어요",
                    context = "게시글을 올리겠습니까?",
                    onConfirm = {
                        onIntent(CommunityIntent.WritePost(state.title, state.content))
                        showWritePostModal = false
                                },
                    onDismiss = { showWritePostModal = false }
                )
            }
        }
    }
}