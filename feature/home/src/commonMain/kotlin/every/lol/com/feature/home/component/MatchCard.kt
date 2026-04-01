package every.lol.com.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus

@Composable
fun MatchCard(
    matchCard: MatchCardModel,
    modifier: Modifier = Modifier,
    team1Progress: Float = 0.28f,
    team2Progress: Float = 0.72f,
    statusColor: Color = EveryLoLTheme.color.semanticWarning,
    team1BarColor: Color = EveryLoLTheme.color.teamHLE,
    team2BarColor: Color = EveryLoLTheme.color.teamGen,
    onClick: (Long) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(matchCard.matchId) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = EveryLoLTheme.color.newBg
        ),
        border = BorderStroke(
            1.dp,
            EveryLoLTheme.color.grayScale900
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = matchCard.seasonName,
                        color = EveryLoLTheme.color.grayScale100,
                        style = EveryLoLTheme.typography.title01
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                       matchCard.groupName
                            ?.takeIf { it.isNotBlank() }
                            ?.let { groupName ->
                                MatchCardTag(text = groupName)
                            }
                        MatchCardTag(text = matchCard.roundName)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MatchCardBarRow(
                    teamName = matchCard.team1Name,
                    progress = team1Progress,
                    barColor = team1BarColor
                )

                MatchCardBarRow(
                    teamName = matchCard.team2Name,
                    progress = team2Progress,
                    barColor = team2BarColor
                )
            }
        }
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
private fun MatchCardBarRow(
    teamName: String,
    progress: Float,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamName,
            style = EveryLoLTheme.typography.body03,
            color = EveryLoLTheme.color.grayScale100,
            modifier = Modifier.width(57.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(22.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(barColor)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMatchCard(){
    val dummyMatchCard = MatchCardModel(
        matchId = 1L,
        seasonName = "2026 Road to MSI",
        team1Name = "HLE",
        team2Name = "Gen",
        groupName = "Baron Elder",
        roundName = "플레이오프 1라운드",
        matchDate = "",
        matchStatus = MatchStatus.LIVE,
        team1Id = 1,
        team2Id = 2,
        team1VoteRate = 0.0,
        team2VoteRate = 0.0,
        totalVoteCount = 0,
        predictedWinnerTeamName = "",
    )
    MatchCard(
        matchCard = dummyMatchCard,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        team1Progress = 0.28f,
        team2Progress = 0.72f,
        onClick = { }
    )
}