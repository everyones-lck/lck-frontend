package every.lol.com.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.LckStandingTeamModel

@Composable
fun LckRankingSection(
    standings: List<LckStandingTeamModel>,
    favoriteTeamId: Long,
    modifier: Modifier = Modifier,
    onTeamClick: (Long) -> Unit = {},
    cardBackground: @Composable BoxScope.(LckStandingTeamModel) -> Unit = {}
) {
    if (standings.isEmpty()) return

    val favoriteTeam = standings.firstOrNull { it.teamId == favoriteTeamId }
    val top3 = standings
        .filter { it.rank in 1..3 }
        .sortedBy { it.rank }

    val lowerRanks = standings
        .filter { it.rank >= 4 }
        .sortedBy { it.rank }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "LCK 순위",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.grayScale800,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "경기가 아직 진행되지 않았습니다",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
/*
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            favoriteTeam?.let { team ->
                FavoriteRankCard(
                    team = team,
                    onClick = onTeamClick,
                    cardBackground = cardBackground
                )
            }

            top3.forEach { team ->
                TopRankCard(
                    team = team,
                    onClick = onTeamClick,
                    cardBackground = cardBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            lowerRanks.forEach { team ->
                RankListRow(
                    team = team,
                    isFavorite = team.teamId == favoriteTeamId,
                    onClick = onTeamClick
                )
            }
        }*/
    }
}

@Composable
private fun FavoriteRankCard(
    team: LckStandingTeamModel,
    onClick: (Long) -> Unit,
    cardBackground: @Composable BoxScope.(LckStandingTeamModel) -> Unit
) {
    Card(
        modifier = Modifier
            .width(76.dp)
            .height(112.dp)
            .clickable { onClick(team.teamId) },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = EveryLoLTheme.color.grayScale1000
        ),
        border = BorderStroke(1.dp, EveryLoLTheme.color.teamT1)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            cardBackground(team)

            Text(
                text = "${team.rank}",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200,
                modifier = Modifier.padding(start = 8.dp, top = 7.dp)
            )

            Text(
                text = team.teamName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun TopRankCard(
    team: LckStandingTeamModel,
    onClick: (Long) -> Unit,
    cardBackground: @Composable BoxScope.(LckStandingTeamModel) -> Unit
) {
    Card(
        modifier = Modifier
            .width(76.dp)
            .height(112.dp)
            .clickable { onClick(team.teamId) },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = EveryLoLTheme.color.grayScale1000
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            cardBackground(team)

            Text(
                text = "${team.rank}",
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.grayScale600,
                modifier = Modifier.padding(start = 8.dp, top = 7.dp)
            )

            Text(
                text = team.teamName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun RankListRow(
    team: LckStandingTeamModel,
    isFavorite: Boolean,
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isFavorite) EveryLoLTheme.color.grayScale1000 else Color.Transparent
            )
            .clickable { onClick(team.teamId) }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${team.rank}",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier.width(10.dp)
        )

        Spacer(modifier = Modifier.width(30.dp))

        Text(
            text = team.teamName,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = team.rightText,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200,
        )
    }
}