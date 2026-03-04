package every.lol.com.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import everylol.feature.intro.generated.resources.Res
import everylol.feature.intro.generated.resources.ic_logo_text
import everylol.feature.intro.generated.resources.img_login
import org.jetbrains.compose.resources.painterResource

@Composable
private fun LoginScreen(
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.everylolDefault(EveryLoLTheme.color.newBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .width(225.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onLongClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                painter = painterResource(Res.drawable.ic_logo_text),
                contentDescription = "everylol logo",
                tint = Color.White
            )
        }

        Image(
            modifier = Modifier
                .width(270.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLongClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            painter = painterResource(Res.drawable.img_login),
            contentDescription = "login image",
        )
    }
}