package every.lol.com.feature.matches.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_back_btn
import org.jetbrains.compose.resources.painterResource

@Composable
fun MatchesHeaderBar(
    title: String = "Today’s Matches",
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 36.dp, top = 20.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_back_btn),
            contentDescription = "뒤로가기",
            tint = EveryLoLTheme.color.grayScale200,
            modifier = Modifier
                .width(9.dp)
                .height(18.dp)
                .clickable(onClick = onBackClick)
        )
        Spacer(modifier = Modifier.width(28.dp))

        Text(
            text = title,
            color = EveryLoLTheme.color.grayScale200,
            style = EveryLoLTheme.typography.heading01
        )
    }

}