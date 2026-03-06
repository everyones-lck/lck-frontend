package every.lol.com.feature.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.ic_logo_text
import org.jetbrains.compose.resources.painterResource


@Composable
internal fun LoadingScreen() {
    Box(
        modifier = Modifier.everylolDefault(EveryLoLTheme.color.newBg).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_logo_text),
            contentDescription = "everylol logo",
            tint = Color.Unspecified
        )
    }
}