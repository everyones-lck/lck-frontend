package every.lol.com.feature.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.feature.mypage.model.MypageUiState

@Composable
fun MyVotedMatchItem(
    mvp: MypageUiState.MVPItem,
    onClick: (Int) -> Unit
) {
    val team = Team.fromTeamId(mvp.playerTeam)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable{ onClick(mvp.id)},
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2026 LCK", //Todo: POG, POM API 수정 후 반영
                style = EveryLoLTheme.typography.heading02,
                color = EveryLoLTheme.color.grayScale100
            )
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