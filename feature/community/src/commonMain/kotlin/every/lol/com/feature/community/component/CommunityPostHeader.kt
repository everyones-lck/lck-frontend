package every.lol.com.feature.community.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.ProfileImage

@Composable
fun CommunityPostHeader(
    profile: String?,
    nickname: String,
    date: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        ProfileImage(
            modifier = Modifier.size(32.dp),
            profile = profile
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = nickname,
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200
            )
            Text(
                text = date,
                style = EveryLoLTheme.typography.caption01,
                color = EveryLoLTheme.color.community600
            )
        }
    }
}