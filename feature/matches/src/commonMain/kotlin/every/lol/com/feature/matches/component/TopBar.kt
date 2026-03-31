package every.lol.com.feature.matches.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 20.dp, bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Today’s Matches",
            color = EveryLoLTheme.color.grayScale200,
            style = EveryLoLTheme.typography.heading01
        )
    }

}