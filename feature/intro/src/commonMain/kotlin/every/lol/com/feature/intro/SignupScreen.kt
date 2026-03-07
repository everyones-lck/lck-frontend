package every.lol.com.feature.intro

import androidx.compose.foundation.background
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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.core.model.TosType
import every.lol.com.core.ui.component.NicknameSection
import every.lol.com.core.ui.component.TeamGroup
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.intro.component.Profile
import every.lol.com.feature.intro.component.SignupBottomSheet
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    nickName: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = false,
    checkNickName: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onNavigateToTermDetail: (Int) -> Unit = {}
) {
    // 1. 상태 간소화: 약관 관련 복잡한 로직은 이제 BottomSheet 내부에서 처리합니다.
    var showTermsSheet by remember { mutableStateOf(false) }
    var isNicknameValid by remember { mutableStateOf(false) }
    var selectedTeams by remember { mutableStateOf(setOf<Team>()) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        showTermsSheet = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "회원가입")
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
                    Profile(requiredGallery = true)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    NicknameSection(
                        nickName = nickName,
                        onValueChange = onValueChange,
                        isDuplicateChecked = enabled,
                        onCheckNickName = { checkNickName(nickName) },
                        onValidationChanged = { isNicknameValid = it }
                    )

                    Spacer(modifier = Modifier.height(56.dp))
                    Text(
                        "응원 팀을 선택해주세요",
                        style = EveryLoLTheme.typography.subtitle03,
                        color = EveryLoLTheme.color.grayScale200
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TeamGroup { selectedTeams = it }
                }
            }
        }

        // 바텀시트가 닫힌 상태에서만 '다음' 버튼 표시
        if (!showTermsSheet) {
            EverylolButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                text = "다음",
                enabled = isNicknameValid, // 닉네임 유효성만 체크
                onClick = onSignupClick
            )
        }

        // 2. 통합된 SignupBottomSheet 호출
        if (showTermsSheet) {
            SignupBottomSheet(
                tosList = TosType.entries,
                isFixed = true,
                onNavigateToTermDetail = { id ->
                    showTermsSheet = false
                    onNavigateToTermDetail(id)
                },
                onDismissRequest = { showTermsSheet = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SignupScreenPreview() {
    EveryLoLTheme {
        SignupScreen(
            nickName = "에브리롤",
            onValueChange = {},
            enabled = true,
            checkNickName = {},
            onBackClick = {},
            onSignupClick = {},
            onNavigateToTermDetail = {}
        )
    }
}