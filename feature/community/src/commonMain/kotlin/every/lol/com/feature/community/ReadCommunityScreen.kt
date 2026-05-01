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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import every.lol.com.core.common.EveryLolBackHandler
import every.lol.com.core.designsystem.component.EverylolBottomInputBar
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.CommentList
import every.lol.com.core.model.mapToUiState
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.CommunityReportModal
import every.lol.com.feature.community.component.ReadComment
import every.lol.com.feature.community.component.ReadPost
import every.lol.com.feature.community.component.VideoPlayerView
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_x
import kotlinx.coroutines.delay
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReadRoute(
    postId: Int,
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit
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
    if (currentState is CommunityUiState.Read && currentState.postId == postId) {
        ReadCommunityScreen(
            postId = postId,
            state = currentState,
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            onIntent = viewModel::onIntent,
            isLiked = currentState.isLiked,
            likeCount = currentState.likeCount
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
                is CommunityEvent.DeletePostSuccess -> onBackClick()
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReadCommunityScreen(
    postId: Int,
    state: CommunityUiState.Read,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onIntent: (CommunityIntent) -> Unit,
    isLiked: Boolean = false,
    likeCount: Int = 0
) {
    var showReportModal by remember { mutableStateOf(false) }
    var reportTargetType by remember { mutableStateOf("") }
    var reportTargetId by remember { mutableStateOf(0) }
    var reportReason by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedMedia by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    var isPostMenuExpanded by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val isKeyboardOpen = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    var replyingTo by remember { mutableStateOf<CommentList?>(null) }
    fun String.truncate(maxChar: Int): String {
        return if (this.length > maxChar) this.take(maxChar) + "..." else this
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(replyingTo) {
        if (replyingTo != null) {
            delay(100)
            focusRequester.requestFocus()
        }
    }

    EveryLolBackHandler(enabled = selectedMedia != null || isPostMenuExpanded) {
        if (selectedMedia != null) {
            selectedMedia = null
        } else {
            isPostMenuExpanded = false
        }
    }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            isRefreshing = false
        }
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
                        modifier = Modifier.focusRequester(focusRequester),
                        value = commentText,
                        onValueChange = { commentText = it },
                        hint = "댓글을 입력해주세요",
                        onDone = {
                            if (commentText.isNotBlank()) {
                                if (replyingTo != null) {
                                    onIntent(
                                        CommunityIntent.WriteReply(
                                            postId,
                                            replyingTo!!.commentId.toLong(),
                                            commentText
                                        )
                                    )
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
                val safeCommentList = postDetail.commentList ?: emptyList()
                val isCommented = safeCommentList.any { it.isWriter }

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        onIntent(CommunityIntent.DetailPost(postId, isRefresh = true))
                    },
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            containerColor = EveryLoLTheme.color.grayScale800,
                            color = EveryLoLTheme.color.grayScale1000,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
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
                                onImageClick = { url -> selectedMedia = url to false },
                                onVideoClick = { url -> selectedMedia = url to true },
                                onLikeClick = { onIntent(CommunityIntent.LikePost(postId)) },
                                isCommented = isCommented,
                                isLiked = isLiked,
                                likeCount = likeCount
                            )
                        }

                        val safeCommentList = postDetail.commentList ?: emptyList()

                        safeCommentList.forEach { parentComment ->
                            item(key = "parent_${parentComment.commentId}") {
                                ReadComment(
                                    comment = parentComment,
                                    isReply = false,
                                    onClick = { replyingTo = parentComment },
                                    onDelete = { onIntent(CommunityIntent.DeleteComment(parentComment.commentId)) },
                                    onReport = {
                                        reportTargetType = "댓글"
                                        reportTargetId = parentComment.commentId
                                        showReportModal = true
                                    }
                                )
                            }

                            items(
                                items = parentComment.replies ?: emptyList(),
                                key = { "reply_${it.commentId}" }
                            ) { reply ->
                                ReadComment(
                                    comment = reply,
                                    isReply = true,
                                    onClick = { },
                                    onDelete = { onIntent(CommunityIntent.DeleteComment(reply.commentId)) },
                                    onReport = {
                                        reportTargetType = "댓글"
                                        reportTargetId = reply.commentId
                                        showReportModal = true
                                    }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        item { Spacer(Modifier.height(12.dp)) }
                    }
                }
            }
        }

        if (isPostMenuExpanded) {
            PostMenu(
                isMine = state.isMine,
                onDismiss = { isPostMenuExpanded = false },
                onDelete = { onIntent(CommunityIntent.DeletePost(postId)) },
                onEdit = {
                    state.post?.let { post ->
                        val sortedBlocks = post.blocks.sortedBy { it.sequence }

                        val combinedContent = sortedBlocks
                            .filter { it.type == "TEXT" }
                            .joinToString("\n") { it.content ?: "" }

                        val mediaItems = sortedBlocks
                            .filter { it.type == "IMAGE" || it.type == "VIDEO" }
                            .mapIndexed { index, block ->
                                CommunityUiState.MediaItem(
                                    id = block.fileName ?: block.fileUrl ?: "media_$index",
                                    uriString = block.fileUrl ?: "",
                                    isVideo = block.type == "VIDEO",
                                    order = block.sequence
                                )
                            }

                        onIntent(
                            CommunityIntent.EditPost(
                                postId = postId,
                                title = post.postTitle,
                                content = combinedContent,
                                medias = mediaItems
                            )
                        )
                        onEditClick(postId)
                        println("DEBUG_LOG: 수정하기 클릭됨, postId: $postId")
                    }
                    isPostMenuExpanded = false
                },
                onReport = {
                    reportTargetType = "게시글"
                    reportTargetId = postId
                    showReportModal = true
                    isPostMenuExpanded = false
                }
            )
        }
        selectedMedia?.let { (url, isVideo) ->
            FullScreenMediaViewer(
                mediaUrl = url,
                isVideo = isVideo,
                onDismiss = { selectedMedia = null }
            )
        }

        if (showReportModal) {
            CommunityReportModal(
                reportType = reportTargetType,
                value = reportReason,
                onValueChange = { reportReason = it },
                onDismiss = {
                    showReportModal = false
                    reportReason = ""
                },
                onReport = {
                    if (reportReason.isNotBlank()) {
                        if (reportTargetType == "게시글") {
                            onIntent(CommunityIntent.ReportPost(reportTargetId, reportReason))
                        } else {
                            onIntent(CommunityIntent.ReportComment(reportTargetId, reportReason))
                        }
                        showReportModal = false
                        reportReason = ""
                    }
                }
            )
        }
    }
}

@Composable
fun PostMenu(
    isMine: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, end = 16.dp)
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopEnd).size(0.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .background(EveryLoLTheme.color.black900,RoundedCornerShape(12.dp)),
                offset = DpOffset(x = (0).dp, y = 0.dp)
            ) {
                if (isMine) {
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(80.dp)
                            .height(36.dp),
                        text = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "수정하기",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        onClick = { onEdit(); onDismiss() }
                    )
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(80.dp)
                            .height(36.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "삭제하기",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        onClick = { onDelete(); onDismiss() }
                    )
                } else {
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "신고하기",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        onClick = { onReport(); onDismiss() }
                    )
                }
            }
        }
    }
}
@Composable
fun FullScreenMediaViewer(
    mediaUrl: String,
    isVideo: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.grayScale900.copy(alpha = 0.9f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        if (isVideo) {
            VideoPlayerView(
                url = mediaUrl,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            AsyncImage(
                model = mediaUrl,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .heightIn(max = 616.dp),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }

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