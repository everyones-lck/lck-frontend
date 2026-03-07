package every.lol.com.feature.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.core.ui.component.NicknameSection
import every.lol.com.core.ui.component.TeamGroup
import every.lol.com.core.ui.ext.customInsets
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.intro.component.Profile

@Composable
fun SignupScreen(
    nickName: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = false,
    checkNickName: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    var selectedTeams by remember { mutableStateOf(setOf<Team>()) }
    var isNicknameValid by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().everylolDefault(EveryLoLTheme.color.newBg).customInsets(top = true),
    ) {
        EverylolTopAppBar(onBackClick = onBackClick, title = "회원가입")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 56.dp),
            contentAlignment = Alignment.Center
        ){
            Profile(requiredGallery = true)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
        ){
            NicknameSection(
                nickName = nickName,
                onValueChange = onValueChange,
                isDuplicateChecked = enabled,
                onCheckNickName = { checkNickName(nickName) },
                onValidationChanged = { isNicknameValid = it }
            )

            Spacer(modifier = Modifier.height(56.dp))
            Text("응원 팀을 선택해주세요", style = EveryLoLTheme.typography.subtitle03, color = EveryLoLTheme.color.grayScale200)
            Spacer(modifier = Modifier.height(24.dp))
            TeamGroup { selectedTeams = it }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ){
        EverylolButton(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxWidth(),
            text = "다음",
            enabled = isNicknameValid,
            onClick = onSignupClick
        )
    }
}