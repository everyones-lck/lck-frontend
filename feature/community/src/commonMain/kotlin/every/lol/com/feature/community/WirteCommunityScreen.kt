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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.common.getMediaMetadata
import every.lol.com.core.common.isVideoUri
import every.lol.com.core.common.rememberMultiResourcePickerLauncher
import every.lol.com.core.common.rememberPlatformContext
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTextField
import every.lol.com.core.designsystem.component.EverylolToastHost
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.community.component.CommunityContentWriteBlock
import every.lol.com.feature.community.component.CommunityImageAdd
import every.lol.com.feature.community.component.TabBar
import every.lol.com.feature.community.component.TitleText
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WriteRoute(
    postId: Int?= null,
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = uiState
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(postId) {
        println(">>> [DEBUG_COMMUNITY] LaunchedEffect Start (postId: $postId)")

        val currentState = viewModel.uiState.value
        println(">>> [DEBUG_COMMUNITY] Current State Type: ${currentState::class.simpleName}")

        if (postId != null) {
            println(">>> [DEBUG_COMMUNITY] Calling LoadPostForEdit($postId)")
            viewModel.onIntent(CommunityIntent.LoadPostForEdit(postId))
        } else {
            if (currentState !is CommunityUiState.Write) {
                println(">>> [DEBUG_COMMUNITY] Setting default WriteTab")
                viewModel.onIntent(CommunityIntent.ClickWriteTab(CommunityUiState.WriteTab.TALK))
            }
        }
    }


    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityEvent.WriteSuccess -> {
                    focusManager.clearFocus()
                    onBackClick()
                }
                is CommunityEvent.ShowToast -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        (uiState as? CommunityUiState.Write)?.let { state ->
            WriteCommunityScreen(
                state = state,
                onBackClick = onBackClick,
                onIntent = viewModel::onIntent,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
fun WriteCommunityScreen(
    state: CommunityUiState.Write,
    onBackClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val isEditMode = state.postId != null
    val scope = rememberCoroutineScope()
    val isFormValid = state.title.isNotBlank() && state.content.isNotBlank()
    var showWritePostModal by remember { mutableStateOf(false) }
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = keyboardHeight > 0
    val context = rememberPlatformContext()

    val mediaPickerLauncher = rememberMultiResourcePickerLauncher { results ->
        scope.launch(Dispatchers.Default) {
            val currentImages = state.selectedMedias.filter { !it.isVideo }
            val currentVideos = state.selectedMedias.filter { it.isVideo }

            val lines = state.content.split("\n")
            val lastLineIndex = lines.lastIndex

            val newMediaList = mutableListOf<CommunityUiState.MediaItem>()
            var imageCountInBatch = 0
            var videoCountInBatch = 0

            results.forEach { result ->
                val uriString = result.toString()

                val isVideo = isVideoUri(result, context)

                if (isVideo && (currentVideos.size + videoCountInBatch < 2)) {
                    val metadata = getMediaMetadata(context, uriString)
                    newMediaList.add(
                        CommunityUiState.MediaItem(
                            id = "vid_${Clock.System.now().toEpochMilliseconds()}",
                            uriString = uriString,
                            thumbnail = metadata.thumbnail,
                            isVideo = true,
                            durationMs = metadata.durationMs,
                            order = lastLineIndex
                        )
                    )
                    videoCountInBatch++
                } else if (!isVideo && (currentImages.size + imageCountInBatch < 10)) {
                    newMediaList.add(
                        CommunityUiState.MediaItem(
                            id = "img_${Clock.System.now().toEpochMilliseconds()}_$imageCountInBatch", // ID 중복 방지
                            uriString = uriString,
                            thumbnail = null,
                            isVideo = false,
                            durationMs = 0L,
                            order = if (state.content.isEmpty()) 0 else lines.size - 1
                        )
                    )
                    imageCountInBatch++
                }
            }
            if (newMediaList.isNotEmpty()) {
                onIntent(CommunityIntent.AddMedias(newMediaList))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg, false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { EverylolToastHost(snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = if(isEditMode) "글 수정하기" else "글 작성하기")
            },
            bottomBar = {
                if (isKeyboardVisible) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    ) {
                        CommunityImageAdd(
                            openGallery = { mediaPickerLauncher() }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TitleText("내용")
                    CommunityContentWriteBlock(
                        state = state,
                        onIntent = onIntent
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
            EverylolButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                text = if(isEditMode) "수정 완료" else "게시글 올리기",
                enabled = isFormValid,
                onClick = { showWritePostModal = true }
            )
            if (showWritePostModal) {
                EverylolModal(
                    title = if(isEditMode) "게시글을 수정할까요?" else "게시글이 완성되었어요",
                    context = if (isEditMode) "수정된 내용은 즉시 반영됩니다." else "게시글을 올리겠습니까?",
                    onConfirm = {
                        onIntent(CommunityIntent.WritePost(
                            context,
                            state.title,
                            state.content,
                            state.selectedMedias
                        ))
                        showWritePostModal = false
                    },
                    onDismiss = { showWritePostModal = false }
                )
            }
        }
    }
}