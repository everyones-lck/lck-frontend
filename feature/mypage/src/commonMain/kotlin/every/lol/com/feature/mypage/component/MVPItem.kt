package every.lol.com.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.core.ui.component.getTeamBrush
import every.lol.com.feature.mypage.model.MypageUiState

@Composable
fun MVPItem(
    mvp: MypageUiState.MVPItem
) {
    val team = Team.fromId(mvp.playerTeam)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(4.dp,12.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(team.getTeamBrush())
                )
                Text(
                    text = mvp.playerName,
                    style = EveryLoLTheme.typography.heading02,
                    color = EveryLoLTheme.color.white200
                )
            }
            Text(
                text = mvp.matchDate,
                style = EveryLoLTheme.typography.label03,
                color = EveryLoLTheme.color.white200
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = EveryLoLTheme.color.grayScale900
        )
    }
}