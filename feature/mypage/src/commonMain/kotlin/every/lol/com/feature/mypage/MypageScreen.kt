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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.ProfileImage
import every.lol.com.core.ui.component.TeamGroup
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.MypageMenuSection
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun MypageRoute(
    viewModel: MypageViewModel = koinViewModel(MypageViewModel::class),
    onBackClick: () -> Unit,
    onNavigate: (MypageUiState.MypageMenuType) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                MypageEvent.NavigateHome -> onBackClick()
                MypageEvent.NavigateProfileEdit -> onNavigate(MypageUiState.MypageMenuType.PROFILE_EDIT)
                MypageEvent.NavigateToMyPosts -> onNavigate(MypageUiState.MypageMenuType.POST_COMMENT)
                MypageEvent.NavigateToPogVote -> onNavigate(MypageUiState.MypageMenuType.POG_VOTE)
                MypageEvent.NavigateToPrediction -> onNavigate(MypageUiState.MypageMenuType.PREDICTION)
                MypageEvent.NavigateWithdrawal -> onNavigate(MypageUiState.MypageMenuType.WITHDRAWAL)
                MypageEvent.NavigateToAppInfo -> onNavigate(MypageUiState.MypageMenuType.APP_INFO)
                MypageEvent.NavigateTos1 -> onNavigate(MypageUiState.MypageMenuType.TOS_1)
                MypageEvent.NavigateTos2 -> onNavigate(MypageUiState.MypageMenuType.TOS_2)
                is MypageEvent.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(event.throwable.message ?: "에러 발생")
                }
            }
        }
    }
    MypageScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = { viewModel.onIntent(MypageIntent.ClickBackToHome) },
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun MypageScreen(
    state: MypageUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onIntent: (MypageIntent) -> Unit
){
    val myPageState = (state as? MypageUiState.Mypage) ?: return
    val myInform = myPageState.myInform
    val menuList = myPageState.menuList

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
                    Text(myInform.nickName,style=EveryLoLTheme.typography.title02, color = EveryLoLTheme.color.grayScale200)
                    TeamGroup(
                        isSelectable = false,
                        selectedTeams = myInform.teamId
                    )
                }
                MypageMenuSection(
                    modifier = Modifier.fillMaxWidth(),
                    menuItems = menuList,
                    onMenuClick = { type ->
                        onIntent(MypageIntent.ClickMenu(type))
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