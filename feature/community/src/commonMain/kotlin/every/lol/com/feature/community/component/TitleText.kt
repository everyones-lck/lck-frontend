package every.lol.com.feature.community.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import every.lol.com.core.designsystem.theme.EveryLoLTheme


@Composable
fun TitleText(
    text: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        text = text,
        style = EveryLoLTheme.typography.subtitle03,
        color = EveryLoLTheme.color.community600
    )
}
