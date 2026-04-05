package every.lol.com.feature.aboutlck.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.aboutlck.generated.resources.Res
import everylol.feature.aboutlck.generated.resources.ic_arrow_right_double
import org.jetbrains.compose.resources.painterResource

@Composable
fun DateSelectSection(
    modifier: Modifier = Modifier,
    date: String?="YYYY-MM-DD",
    showDatePicker: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
        ){
        Text(
            text = date ?: "YYYY-MM-DD",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.community600
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = "다른 날 선택",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.community600
            )
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_right_double),
                modifier = Modifier
                    .size(14.dp).clickable(
                    onClick = showDatePicker
                ),
                contentDescription = null,
                tint = EveryLoLTheme.color.community600
                )
        }
    }
}