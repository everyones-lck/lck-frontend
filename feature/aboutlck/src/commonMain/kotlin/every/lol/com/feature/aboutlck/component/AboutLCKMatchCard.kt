package every.lol.com.feature.aboutlck.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.aboutlck.match.MatchDetail
import every.lol.com.core.ui.component.CardTag
import every.lol.com.core.ui.component.getTeamColor

@Composable
fun AboutLCKMatchCard (
    item: MatchDetail,
    modifier: Modifier = Modifier
) {
    Box(
    modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(EveryLoLTheme.color.newBg)
        .border(
            width = 1.dp,
            color = EveryLoLTheme.color.grayScale900,
            shape = RoundedCornerShape(8.dp)
        )
        .padding(20.dp, 28.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.seasonName,
                color = EveryLoLTheme.color.grayScale100,
                style = EveryLoLTheme.typography.heading01
            )

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.groupName
                        ?.takeIf { it.isNotBlank() }
                        ?.let { groupName ->
                            CardTag(text = groupName)
                        }
                    item.roundName
                        ?.takeIf { it.isNotBlank() }
                        ?.let { roundName ->
                            CardTag(text = roundName)
                        }
                }
                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CardTag(
                        text = item.team1.teamName,
                        backgroundColor = getTeamColor(item.team1.teamName),
                        textColor = EveryLoLTheme.color.black900
                    )
                    CardTag(
                        text = item.team2.teamName,
                        backgroundColor = getTeamColor(item.team2.teamName),
                        textColor = EveryLoLTheme.color.black900
                    )
                }
            }

        }
    }
}