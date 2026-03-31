package every.lol.com.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.component.EverylolBottomInputBar
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.CommentList
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
    var isPostMenuExpanded by remember { mutableStateOf(false) }
    var selectedComment by remember { mutableStateOf<CommentList?>(null) }
    var commentText by remember { mutableStateOf("") }
    val isKeyboardOpen = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    var replyingTo by remember { mutableStateOf<CommentList?>(null) }
    fun String.truncate(maxChar: Int): String {
        return if (this.length > maxChar) this.take(maxChar) + "..." else this
    }

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
                    replyingTo?.let { target ->
                        val borderColor = EveryLoLTheme.color.grayScale900
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(EveryLoLTheme.color.grayScale1000)
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = strokeWidth
                                    )
                                }
                                .padding(24.dp, 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = target.content.truncate(15),
                                style = EveryLoLTheme.typography.pretendardBody02,
                                color = EveryLoLTheme.color.white200
                            )
                            Text(
                                text = "의 대댓글 작성 중",
                                style = EveryLoLTheme.typography.pretendardBody02,
                                color = EveryLoLTheme.color.gray800
                            )
                        }
                    }
                    EverylolBottomInputBar(
                        value = commentText,
                        onValueChange = { commentText = it },
                        hint = "댓글을 입력해주세요",
                        onDone = {
                            if (commentText.isNotBlank()) {
                                if (replyingTo != null) {
                                    onIntent(CommunityIntent.WriteReply(postId, replyingTo!!.commentId.toLong(), commentText))
                                } else {
                                    onIntent(CommunityIntent.WriteComment(postId, commentText))
                                }
                                commentText = ""
                                replyingTo = null
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ReadPost(
                            postDetail = postDetail,
                            contentBlocks = mapToUiState(postDetail),
                            onMoreClick = { isPostMenuExpanded = true },
                            onImageClick = { selectedImageUrl = it },
                            onVideoClick = {}
                        )
                    }
                    items(postDetail.commentList.size) { index ->
                        val comment = postDetail.commentList[index]
                        ReadComment(
                            comment =comment,
                            onMoreClick = { selectedComment = comment },
                            onClick = {replyingTo = comment}
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    item{Spacer(Modifier.height(12.dp))}
                }
            }
        }
        if (isPostMenuExpanded) {
            PostMenu(
                isMine = state.isMine,
                onDismiss = { isPostMenuExpanded = false },
                onDelete = { onIntent(CommunityIntent.DeletePost(postId)) },
                onReport = { onIntent(CommunityIntent.ReportPost(postId)) }
            )
        }

        selectedComment?.let { comment ->
            CommentMenu(
                isMine = comment.isWriter,
                onDismiss = { selectedComment = null },
                onDelete = {
                    onIntent(CommunityIntent.DeleteComment( comment.commentId))
                },
                onReport = {
                    onIntent(CommunityIntent.ReportComment( comment.commentId))
                }
            )
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
fun PostMenu(
    isMine: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 56.dp, end = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = onDismiss,
            modifier = Modifier.background(EveryLoLTheme.color.grayScale800)
        ) {
            if (isMine) {
                DropdownMenuItem(
                    text = { Text("게시글 삭제하기", color = EveryLoLTheme.color.white200) },
                    onClick = { onDelete(); onDismiss() }
                )
            } else {
                DropdownMenuItem(
                    text = { Text("게시글 신고하기", color = EveryLoLTheme.color.white200) },
                    onClick = { onReport(); onDismiss() }
                )
            }
        }
    }
}

@Composable
fun CommentMenu(
    isMine: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = onDismiss,
            modifier = Modifier.background(EveryLoLTheme.color.grayScale800)
        ) {
            if (isMine) {
                DropdownMenuItem(
                    text = { Text("댓글 삭제하기", color = EveryLoLTheme.color.white200) },
                    onClick = { onDelete(); onDismiss() }
                )
            } else {
                DropdownMenuItem(
                    text = { Text("댓글 신고하기", color = EveryLoLTheme.color.white200) },
                    onClick = { onReport(); onDismiss() }
                )
            }
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