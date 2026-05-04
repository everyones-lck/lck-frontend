package every.lol.com.feature.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.ProfileImage
import every.lol.com.core.ui.component.TeamGroup
import every.lol.com.core.ui.component.TosScreen
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.MypageMenuSection
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState
import moe.tlaster.precompose.koin.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MypageRoute(
    viewModel: MypageViewModel = koinViewModel(MypageViewModel::class),
    onBackClick: () -> Unit,
    onLogoutSuccess:() -> Unit,
    onWithdrawalSuccess:() -> Unit,
    navToCommunityRead: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(MypageIntent.LoadMypage)
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                MypageEvent.NavigateHome -> onBackClick()
                MypageEvent.Logout -> onLogoutSuccess()
                MypageEvent.Withdrawal -> onWithdrawalSuccess()
                is MypageEvent.NavigateToCommentDetail -> navToCommunityRead(event.postId)
                is MypageEvent.NavigateToPostDetail -> navToCommunityRead(event.postId)
                is MypageEvent.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(event.throwable.message ?: "에러 발생")
                }
                else -> {}
            }
        }
    }

    when (uiState) {
        is MypageUiState.Mypage -> {
            MypageScreen(
                state = uiState as MypageUiState.Mypage,
                onBackClick = {
                    if (uiState is MypageUiState.Mypage) {
                        onBackClick()
                    } else {
                        viewModel.onIntent(MypageIntent.LoadMypage)
                    }
                },
                onIntent = viewModel::onIntent
            )
        }
        is MypageUiState.AppInform -> {
            MypageAppInformScreen(
                state = uiState as MypageUiState.AppInform,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage) },
                onIntent = viewModel::onIntent
            )
        }
        is MypageUiState.ProfileEdit -> {
            MypageProfileEditScreen(
                state = uiState as MypageUiState.ProfileEdit,
                onValueChange = { name ->
                    viewModel.handleInputNickName(name)
                },
                onTeamsChange = {
                    viewModel.updateSelectedTeams(it)
                },
                onProfileImageChange = {
                    viewModel.updateProfileImage(it)
                },
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage) },
                onIntent = viewModel::onIntent
            )
        }
        is MypageUiState.Community -> {
            MypageCommunityScreen(
                state = uiState as MypageUiState.Community,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage) },
                onIntent = viewModel::onIntent
            )
        }
        is MypageUiState.Prediction -> {
            MypagePredictionScreen(
                state = uiState as MypageUiState.Prediction,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage) }
            )
        }
        is MypageUiState.MVP -> {
            MypageMVPScreen(
                state = uiState as MypageUiState.MVP,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage) },
                onIntent = viewModel::onIntent
            )
        }
        is MypageUiState.Loading -> {

        }
        is MypageUiState.Withdrawal -> {
            MypageWithdrawalScreen(
                state = uiState as MypageUiState.Withdrawal,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadMypage)},
                onWithdrawalConfrimClick = {
                    viewModel.onIntent(MypageIntent.Withdrawal)
                }
            )
        }
        is MypageUiState.TosDetail -> {
            TosScreen(
                tosId = (uiState as MypageUiState.TosDetail).id,
                onBackClick = { viewModel.onIntent(MypageIntent.LoadAppInform) }
            )
        }
    }
}

@Composable
private fun MypageScreen(
    state: MypageUiState,
    onBackClick: () -> Unit,
    onIntent: (MypageIntent) -> Unit
){

    val myPageState = (state as? MypageUiState.Mypage) ?: return
    val myInform = myPageState.myInform
    val menuList = myPageState.menuList
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg,false),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "마이페이지")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp, bottom = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProfileImage(profile = myInform.profileImage)
                    Text(
                        myInform.nickName,
                        style=EveryLoLTheme.typography.title02,
                        color = EveryLoLTheme.color.grayScale200
                    )
                    TeamGroup(
                        isSelectable = false,
                        selectedTeams = myInform.teamIds
                    )
                }
                MypageMenuSection(
                    modifier = Modifier.fillMaxWidth(),
                    menuItems = menuList,
                    onMenuClick = { type ->
                        if (type == MypageUiState.MypageMenuType.LOGOUT) {
                            showLogoutModal = true
                        } else {
                            onIntent(MypageIntent.ClickMenu(type))
                        }
                    }
                )
            }
        }
        if(showLogoutModal) {
            EverylolModal(
                title = "로그아웃 하시겠습니까?",
                onDismiss = { showLogoutModal = false },
                onConfirm = {
                    showLogoutModal = false
                    onIntent(MypageIntent.ClickMenu(MypageUiState.MypageMenuType.LOGOUT))
                }
            )
        }
    }
}