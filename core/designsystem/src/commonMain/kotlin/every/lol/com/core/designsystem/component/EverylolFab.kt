package every.lol.com.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.designsystem.generated.resources.Res
import everylol.core.designsystem.generated.resources.ic_pencil
import org.jetbrains.compose.resources.painterResource


@Composable
fun EverylolFab(
    modifier: Modifier = Modifier.padding(bottom = 32.dp, end = 32.dp),
    round: Dp = 11.dp,
    contentPadding: PaddingValues = PaddingValues(11.dp),
    backgroundColor: Color = EveryLoLTheme.color.grayScale800,
    contentColor: Color = EveryLoLTheme.color.white200,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.size(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor =backgroundColor,
            disabledContentColor = contentColor
        ),
        shape = RoundedCornerShape(round),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                painter = painterResource(Res.drawable.ic_pencil),
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}