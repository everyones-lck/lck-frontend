package every.lol.com.feature.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.common.rememberImagePickerLauncher
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.core.ui.component.ImageBottomSheet
import every.lol.com.core.ui.component.NicknameSection
import every.lol.com.core.ui.component.ProfileImage
import every.lol.com.core.ui.component.TeamGroup
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState

//TOdo: 추후에 Profile정의 후 SignUpScreen과 Component 전체 공유로 수정
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageProfileEditScreen(
    state: MypageUiState.ProfileEdit,
    onValueChange: (String) -> Unit = {},
    onProfileImageChange: (Any?) -> Unit = {},
    onTeamsChange: (Set<Team>) -> Unit = {},
    checkNickName: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onIntent: (MypageIntent) -> Unit,
) {
    var showImageSheet by remember { mutableStateOf(false) }
    var isNicknameValid by remember { mutableStateOf(false) }
    var showProfileEditModal by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        if (uri != null) {
            onProfileImageChange(uri)
        }
    }

    val isChanged = remember(state.nickName, state.profileImage, state.teamId) {
        val isTeamChanged = state.teamId != state.originalTeamId
        val isImageChanged = when {
            state.profileImage is ByteArray && state.originalProfileImage is ByteArray -> {
                !(state.profileImage contentEquals state.originalProfileImage)
            }
            else -> state.profileImage != state.originalProfileImage
        }
        isTeamChanged || isImageChanged
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar( onBackClick = {
                    if (isChanged) showProfileEditModal = true
                    else onBackClick()
                },  title = "프로필 수정")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileImage(
                        requiredGallery = true,
                        profile = state.profileImage,
                        onOpenGallery = { showImageSheet = true }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    NicknameSection(
                        isProfileEdit = true,
                        originalNickname =  state.originalNickname,
                        nickName = state.nickName,
                        onValueChange = onValueChange,
                        isDuplicateChecked = state.isDuplicateChecked,
                        onCheckNickName = {
                            onIntent(MypageIntent.ClickCheckDuplicateNickname(state.nickName))
                        },
                        onValidationChanged = { isNicknameValid = it }
                    )

                    Spacer(modifier = Modifier.height(56.dp))
                    Text(
                        "응원 팀을 선택해주세요",
                        style = EveryLoLTheme.typography.subtitle03,
                        color = EveryLoLTheme.color.grayScale200
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TeamGroup(
                        selectedTeams = state.teamId,
                        onSelectedTeamsChange = { teams ->
                            onTeamsChange(teams)
                        }
                    )
                }
            }
        }
        EverylolButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxWidth(),
            text = "저장",
            enabled = isChanged || isNicknameValid && !state.isLoading,
            onClick = { onIntent(MypageIntent.SaveProfile) }
        )

        if (showImageSheet) {
            ImageBottomSheet(
                onDismissRequest = { showImageSheet = false },
                onGalleryClick = {
                    imagePickerLauncher()
                },
                onDefaultClick = {
                    onProfileImageChange(null)
                }
            )
        }

        if(showProfileEditModal) {
            EverylolModal(
                title = "앗, 프로필이  저장되지 않았어요",
                context = "변경된 프로필을 저장하지 않고 나가시겠습니까?",
                confirmText = "나가기",
                onConfirm = onBackClick,
                onDismiss = { showProfileEditModal = false }
            )
        }
    }
}