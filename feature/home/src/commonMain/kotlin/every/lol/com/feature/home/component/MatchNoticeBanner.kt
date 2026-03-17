package every.lol.com.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.home.generated.resources.Res
import everylol.feature.home.generated.resources.ic_close_btn
import org.jetbrains.compose.resources.painterResource

@Composable
fun MatchNoticeBanner(
    message: String,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(EveryLoLTheme.color.grayScale900),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 13.dp)
                .weight(1f)
        )

        IconButton(onClick = onCloseClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_close_btn),
                contentDescription = "닫기",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewNoticeBanner() {
    EveryLoLTheme {
        MatchNoticeBanner(
            message = "안녕하세요",
            onCloseClick = {}
        )
    }
}