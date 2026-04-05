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
import androidx.compose.material3.SnackbarHost
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
    innerPadding : PaddingValues,
    viewModel: CommunityViewModel = koinViewModel(CommunityViewModel::class),
    onBackClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = uiState
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        if (uiState !is CommunityUiState.Write) {
            viewModel.onIntent(CommunityIntent.ClickWriteTab(CommunityUiState.WriteTab.TALK))
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityEvent.WriteSuccess -> {
                    focusManager.clearFocus()
                    onBackClick()
                }
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (currentState is CommunityUiState.Write) {
            WriteCommunityScreen(
                state = currentState,
                onBackClick = onBackClick,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
fun WriteCommunityScreen(
    state: CommunityUiState.Write,
    onBackClick: () -> Unit,
    onIntent: (CommunityIntent) -> Unit
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isFormValid = state.title.isNotBlank() && state.content.isNotBlank()
    var showWritePostModal by remember { mutableStateOf(false) }
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = keyboardHeight > 0
    val context = rememberPlatformContext()

    val mediaPickerLauncher = rememberMultiResourcePickerLauncher { results ->
        // 이 내부(람다)는 더 이상 @Composable 영역이 아니지만,
        // 위에서 정의한 scope와 context를 "캡처"해서 사용할 수 있습니다.
        scope.launch(Dispatchers.Default) {
            val currentImages = state.selectedMedias.filter { !it.isVideo }
            val currentVideos = state.selectedMedias.filter { it.isVideo }
            val currentOrder = (state.content.split("\n").size - 1).coerceAtLeast(0)

            val newMediaList = mutableListOf<CommunityUiState.MediaItem>()
            var imageCountInBatch = 0
            var videoCountInBatch = 0

            results.forEach { result ->
                val uriString = result.toString()

                // 💡 [수정] Context 체크 없이 expect 함수 호출
                val isVideo = isVideoUri(result, context)

                if (isVideo && (currentVideos.size + videoCountInBatch < 2)) {
                    // 비디오 메타데이터 가져오기
                    val metadata = getMediaMetadata(context, uriString)

                    newMediaList.add(
                        CommunityUiState.MediaItem(
                            id = "vid_${Clock.System.now().toEpochMilliseconds()}",
                            uriString = uriString,
                            thumbnail = metadata.thumbnail,
                            isVideo = true, // 💡 이제 정상적으로 true가 들어감
                            durationMs = metadata.durationMs,
                            order = currentOrder
                        )
                    )
                    videoCountInBatch++
                } else if (!isVideo && (currentImages.size + imageCountInBatch < 10)) {
                    // 이미지 처리 로직...
                    newMediaList.add(
                        CommunityUiState.MediaItem(
                            id = "img_${Clock.System.now().toEpochMilliseconds()}",
                            uriString = uriString,
                            thumbnail = null,
                            isVideo = false,
                            durationMs = 0L,
                            order = currentOrder
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
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "글 작성하기")
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
                text = "게시글 올리기",
                enabled = isFormValid,
                onClick = { showWritePostModal = true }
            )
            if (showWritePostModal) {
                EverylolModal(
                    title = "게시글이 완성되었어요",
                    context = "게시글을 올리겠습니까?",
                    onConfirm = {
                        onIntent(
                            CommunityIntent.WritePost(
                                state.title,
                                state.content,
                                state.selectedMedias
                            )
                        )
                        // showWritePostModal = false
                    },
                    onDismiss = { showWritePostModal = false }
                )
            }
        }
    }
}