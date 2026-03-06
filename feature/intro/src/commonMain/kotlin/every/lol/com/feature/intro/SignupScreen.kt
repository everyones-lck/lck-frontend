package every.lol.com.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolTextField
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.core.ui.component.NicknameValidation
import every.lol.com.core.ui.component.TeamChip
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.intro.component.Profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignupScreen(
    nickName: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = false,
    onBackClick: () -> Unit = {},
    onSignupClick: () -> Unit = { }
) {
    val allTeams = Team.entries.filter { it != Team.NONE }
    var selectedTeams by remember { mutableStateOf(setOf<Team>()) }

    Column(
        modifier = Modifier.everylolDefault(EveryLoLTheme.color.newBg),
    ) {
        EverylolTopAppBar(onBackClick = onBackClick, title = "회원가입")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(56.dp),
            contentAlignment = Alignment.Center
        ){
            Profile()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.Start
        ){
            EverylolTextField(
                value = nickName,
                onValueChange = onValueChange,
                hint = "닉네임을 입력해주세요",
                onDone = {
                    if (enabled) onSignupClick()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            NicknameValidation(message = "중복된 닉네임은 사용할 수 없습니다")
            Spacer(modifier = Modifier.height(12.dp))
            NicknameValidation(message = "닉네임은 최대 10글자까지 입력 가능합니다")
            Spacer(modifier = Modifier.height(12.dp))
            NicknameValidation(message = "닉네임 사이에는 공백을 입력할 수 없습니다")
            Spacer(modifier = Modifier.height(56.dp))
            Text("응원 팀을 선택해주세요", style = EveryLoLTheme.typography.subtitle03, color = EveryLoLTheme.color.grayScale200)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                allTeams.forEach { team ->
                    TeamChip(
                        team = team,
                        isSelected = selectedTeams.contains(team),
                        onClick = {
                            selectedTeams = if (selectedTeams.contains(team)) {
                                selectedTeams - team
                            } else {
                                selectedTeams + team
                            }
                        }
                    )
                }
            }
        }

        EverylolButton(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = "다음",
            enabled = nickName.isNotEmpty(),
            onClick = onSignupClick
        )
    }
}