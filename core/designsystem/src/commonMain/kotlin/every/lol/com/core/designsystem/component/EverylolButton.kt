package every.lol.com.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme


@Composable
fun EverylolButton(
    modifier: Modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp).fillMaxWidth(),
    text: String,
    enabled: Boolean = true,
    round: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(15.dp),
    textStyle: TextStyle = EveryLoLTheme.typography.body02,
    backgroundColor: Color = if (enabled) EveryLoLTheme.color.grayScale500 else EveryLoLTheme.color.grayScale900,
    contentColor: Color = EveryLoLTheme.color.grayScale1000,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor =backgroundColor,
            disabledContentColor = contentColor
        ),
        shape = RoundedCornerShape(round),
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = textStyle,
                color = contentColor
            )
        }
    }
}