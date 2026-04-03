package every.lol.com.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.HomeTodayMatch
import every.lol.com.core.model.HomeTodayMatchDetail
import every.lol.com.core.model.MatchVoteRate

@Composable
fun MatchCardRow(
    matchCards: HomeTodayMatch?=null,
    matchRate: Map<Long, MatchVoteRate> = emptyMap(),
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {}
){
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    val matches = matchCards?.matches ?: emptyList()
    LazyRow(
        state = lazyListState,
        flingBehavior = snapFlingBehavior,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        if (matches.isEmpty()) {
            item {
                MatchCard(
                    matchCard = null,
                    voteRate = null,
                    modifier = Modifier.fillParentMaxWidth(1f),
                    onClick = {}
                )
            }
        } else {
            items(matches.size) { index ->
                val match = matches[index]
                MatchCard(
                    matchCard = match,
                    voteRate = matchRate[match.matchId],
                    modifier = Modifier.fillParentMaxWidth(1f),
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun MatchCard(
    matchCard: HomeTodayMatchDetail?,
    voteRate: MatchVoteRate?,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {}
) {

    val team1Progress = voteRate?.team1?.voteRate?.toFloat() ?: 0.5f
    val team2Progress = voteRate?.team2?.voteRate?.toFloat() ?: 0.5f

    val statusColor = when(matchCard?.matchStatus){
        "LIVE" -> EveryLoLTheme.color.semanticWarning
        else -> EveryLoLTheme.color.grayScale800
    }

    Card(
        modifier = modifier
            .then(
                if (matchCard != null) Modifier.clickable { onClick(matchCard.matchId) }
                else Modifier
            ),
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
                        text = if(matchCard != null)matchCard.seasonName else "No Match",
                        color = EveryLoLTheme.color.grayScale100,
                        style = EveryLoLTheme.typography.title01
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {if (matchCard != null) {
                        if(matchCard.groupName != null){
                            MatchCardTag(text = matchCard.groupName)
                            MatchCardTag(text = matchCard.roundName)
                        }
                        MatchCardTag(text = matchCard.roundName)
                    } else {
                        MatchCardTag(text = "")
                        MatchCardTag(text = "")
                    }
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
                    teamName = if (matchCard != null) matchCard.team1.teamName else "-",
                    progress = team1Progress,
                    barColor = if (matchCard == null || (( matchCard.matchStatus == "FINISHED") &&  !matchCard.team1.winner)) {
                        SolidColor(EveryLoLTheme.color.grayScale800)
                    } else {
                        getTeamBrush(matchCard.team1.teamId.toInt())
                    }
                )

                MatchCardBarRow(
                    teamName = if(matchCard != null) matchCard.team2.teamName else ("-"),
                    progress = team2Progress,
                    barColor = if (matchCard == null || (( matchCard.matchStatus == "FINISHED") && !matchCard.team2.winner)) {
                        SolidColor(EveryLoLTheme.color.grayScale800)
                    } else {
                        getTeamBrush(matchCard.team2.teamId.toInt())
                    }
                )
            }
        }
    }
}

@Composable
private fun MatchCardTag(
    text: String?=null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(
                if (text.isNullOrBlank()) Color.Transparent
                else EveryLoLTheme.color.grayScale900
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text ?:"",
            style = EveryLoLTheme.typography.body03,
            color = EveryLoLTheme.color.grayScale600
        )
    }
}

@Composable
private fun MatchCardBarRow(
    teamName: String,
    teamWinner: Boolean ?= true,
    progress: Float,
    barColor: Brush,
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

@Composable
fun getTeamBrush(id : Int): Brush {
    return when (id) {
        1 -> SolidColor(EveryLoLTheme.color.teamGen)
        2 -> SolidColor(EveryLoLTheme.color.teamT1)
        3 -> SolidColor(EveryLoLTheme.color.teamNS)
        4 -> SolidColor(EveryLoLTheme.color.teamDNS)
        5 -> SolidColor(EveryLoLTheme.color.teamBRO)
        6 -> SolidColor(EveryLoLTheme.color.teamBFX)
        7 -> SolidColor(EveryLoLTheme.color.teamDK)
        8 -> SolidColor(EveryLoLTheme.color.teamKRX)
        9 -> SolidColor(EveryLoLTheme.color.teamKT)
        10 -> SolidColor(EveryLoLTheme.color.teamHLE)
        else -> SolidColor(Color.Transparent)
    }
}