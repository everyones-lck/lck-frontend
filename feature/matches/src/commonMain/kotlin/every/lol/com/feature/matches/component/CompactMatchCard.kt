package every.lol.com.feature.matches.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_double_arrow_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun CompactMatchCard (
    item: MatchCardModel,
    modifier: Modifier = Modifier
) {
    Box(
    modifier = modifier
    .width(328.dp)
    .height(118.dp)
    .clip(RoundedCornerShape(8.dp))
    .background(EveryLoLTheme.color.grayScale1000)
    .padding(horizontal = 20.dp)
    .padding(top = 28.dp, bottom = 38.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = item.seasonName,
                color = EveryLoLTheme.color.grayScale100,
                style = EveryLoLTheme.typography.heading01
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item.groupName
                    ?.takeIf { it.isNotBlank() }
                    ?.let { groupName ->
                        MatchCardTag(text = groupName)
                    }
                MatchCardTag(text = item.roundName)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(10.dp)
                .clip(CircleShape)
                .background(matchStatusDotColor(item.matchStatus))
        )
    }
}

@Composable
fun CompactMatchDownCard (
    item: MatchCardModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(328.dp)
            .height(118.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 28.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.seasonName,
                    color = EveryLoLTheme.color.grayScale100,
                    style = EveryLoLTheme.typography.heading01
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item.groupName
                        ?.takeIf { it.isNotBlank() }
                        ?.let { groupName ->
                            MatchCardTag(text = groupName)
                        }
                    MatchCardTag(text = item.roundName)
                }
            }

            Icon(
                painter = painterResource(Res.drawable.ic_double_arrow_down),
                contentDescription = "더보기",
                tint = EveryLoLTheme.color.grayScale600,
                modifier = Modifier.size(width = 8.3.dp, height = 7.8.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 20.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(matchStatusDotColor(item.matchStatus))
        )
    }
}


@Composable
private fun MatchCardTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(EveryLoLTheme.color.grayScale900)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = EveryLoLTheme.typography.body03,
            color = EveryLoLTheme.color.grayScale600
        )
    }
}

@Composable
private fun matchStatusDotColor(status: MatchStatus) = when (status) {
    MatchStatus.SCHEDULED -> EveryLoLTheme.color.grayScale800
    MatchStatus.LIVE -> EveryLoLTheme.color.semanticWarning
    MatchStatus.FINISHED -> EveryLoLTheme.color.grayScale800
}