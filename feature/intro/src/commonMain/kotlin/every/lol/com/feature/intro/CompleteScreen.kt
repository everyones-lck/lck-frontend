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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
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
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.img_complete_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CompleteScreen(
    nickName: String,
    onGoHomeClick:() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .everylolDefault(EveryLoLTheme.color.newBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${nickName}님,반가워요",
                style = EveryLoLTheme.typography.title02,
                color = Color.White
            )
            Text(
                text = "함께 즐기는 LCK 시작해볼까요?",
                style = EveryLoLTheme.typography.heading02,
                color = EveryLoLTheme.color.grayScale400
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.img_complete_logo),
                modifier = Modifier.padding(horizontal = 90.dp).fillMaxWidth(),
                contentDescription = "complete logo"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            EverylolButton(
                modifier = Modifier.fillMaxWidth(),
                text = "홈으로",
                enabled = true,
                onClick = onGoHomeClick
            )
        }
    }
}