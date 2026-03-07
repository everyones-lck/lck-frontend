package every.lol.com.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.designsystem.generated.resources.Res
import everylol.core.designsystem.generated.resources.ic_back

@Composable
fun EverylolTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    isLogo: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onNavigateAlert: (() -> Unit)? = null,
    onNavigateMypage: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(16.dp, 4.dp)
    ) {
        onBackClick?.let {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EverylolIconButton(
                    icon = Res.drawable.ic_back,
                    onClick = onBackClick,
                    size = 36
                )

                title?.let {
                    Text(
                        text = title,
                        style = EveryLoLTheme.typography.heading01,
                        color = EveryLoLTheme.color.grayScale200
                    )

                }
            }
        }

        if (isLogo) {
            Box(
                modifier = Modifier.align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                //로고 아이콘 추가
            }
        }

        if (onNavigateAlert != null && onNavigateMypage != null) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //마이페이지나 알람 추가
            }
        }
    }
}