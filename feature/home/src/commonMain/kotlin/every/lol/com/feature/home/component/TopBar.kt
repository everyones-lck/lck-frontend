package every.lol.com.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.home.generated.resources.Res
import everylol.feature.home.generated.resources.ic_logo
import everylol.feature.home.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBar(
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(EveryLoLTheme.color.newBg)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_logo),
            contentDescription = "로고",
            tint = EveryLoLTheme.color.grayScale100,
            modifier = Modifier
                .width(35.dp)
                .height(18.dp)
        )

        Icon(
            painter = painterResource(Res.drawable.ic_profile),
            contentDescription = "프로필",
            tint = EveryLoLTheme.color.grayScale100,
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onProfileClick)
        )
    }
}

@Preview
@Composable
fun PreviewTopbar(){
    EveryLoLTheme {
        TopBar(
            onProfileClick = {}
        )
    }
}
