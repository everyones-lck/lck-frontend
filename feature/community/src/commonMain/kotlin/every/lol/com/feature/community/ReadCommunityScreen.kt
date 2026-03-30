package every.lol.com.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.component.EverylolBottomInputBar
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.mapToUiState
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.ReadComment
import every.lol.com.feature.community.component.ReadPost
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_x
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReadRoute(
    postId: Int,
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit,
    onDeleteSuccess: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(postId) {
        viewModel.onIntent(CommunityIntent.DetailPost(postId))
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(CommunityIntent.Loading)
        }
    }
    val currentState = uiState
    if (currentState is CommunityUiState.Read) {
        ReadCommunityScreen(
            postId = postId,
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
    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityEvent.NavigateCommunityHome -> onBackClick
                else -> {}
            }
        }
    }
}

@Composable
fun ReadCommunityScreen(
    postId: Int,
    state: CommunityUiState.Read,
    onBackClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val isKeyboardOpen = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.navigationBars,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick)
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .background(EveryLoLTheme.color.newBg)
                        .padding(bottom = if (isKeyboardOpen) 12.dp else 36.dp)
                        .windowInsetsPadding(WindowInsets.ime)
                ) {
                    EverylolBottomInputBar(
                        value = commentText,
                        onValueChange = { commentText = it },
                        hint = "댓글을 입력해주세요",
                        onDone = {
                            if (commentText.isNotBlank()) {
                                onIntent(CommunityIntent.WriteComment(postId, commentText))
                                commentText = ""
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            state.post?.let { postDetail ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        ReadPost(
                            postDetail = postDetail,
                            contentBlocks = mapToUiState(postDetail),
                            onMoreClick = { isMenuExpanded = true },
                            onImageClick = { imageUrl ->
                                selectedImageUrl = imageUrl
                            },
                            onVideoClick = {}
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    items(postDetail.commentList.size) { index ->
                        ReadComment(
                            comment = postDetail.commentList[index],
                            onMoreClick = { isMenuExpanded = true }
                        )
                    }
                    item{Spacer(Modifier.height(20.dp))}
                }
            }
        }
        if (isMenuExpanded) {
            Box(
                modifier = Modifier
                    .padding(top = 100.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .background(
                            color = EveryLoLTheme.color.grayScale800,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    if (state.isMine) {
                        DropdownMenuItem(
                            text = { Text("수정하기", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.white200) },
                            onClick = { isMenuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("삭제하기", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.white200) },
                            onClick = {
                                isMenuExpanded = false
                                onIntent(CommunityIntent.DeletePost(postId))
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text("신고하기", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.white200) },
                            onClick = {
                                isMenuExpanded = false
                                onIntent(CommunityIntent.ReportPost(postId))
                            }
                        )
                    }
                }
            }
        }
        selectedImageUrl?.let { url ->
            FullScreenImageViewer(
                imageUrl = url,
                onDismiss = { selectedImageUrl = null }
            )
        }
    }
}

@Composable
fun FullScreenImageViewer(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.grayScale900.copy(alpha = 0.8f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )


        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_x),
                contentDescription = null,
                tint = EveryLoLTheme.color.white200
            )
        }
    }
}