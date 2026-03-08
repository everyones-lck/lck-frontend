package every.lol.com.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.img_complete_logo
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun CompleteScreen(
    nickName: String,
    onGoHomeClick:() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .everylolDefault(EveryLoLTheme.color.newBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top= 160.dp),
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
                modifier = Modifier.padding(horizontal = 60.dp).fillMaxWidth(),
                contentDescription = "complete logo"
            )
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
            text = "홈으로",
            enabled = true,
            onClick = onGoHomeClick
        )
    }
}