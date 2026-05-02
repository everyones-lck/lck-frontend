package every.lol.com.feature.matches.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus
import every.lol.com.core.model.TodayMatchCard
import every.lol.com.core.model.WinnerSide
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_double_arrow_right
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailMatchCard(
    item: MatchCardModel,
    modifier: Modifier = Modifier,
    onWatchClick: () -> Unit = {},
    onOpenTalkClick: () -> Unit = {},
    onPredictionClick: () -> Unit = {}
) {
    val displayWinnerTeamName = item.actualWinnerTeamName

    Column(
        modifier = modifier
            .width(328.dp)
            .height(284.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .border(1.dp, EveryLoLTheme.color.black900, RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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

                    displayWinnerTeamName
                        ?.takeIf { it.isNotBlank() }
                        ?.let { winnerTeamName ->
                            MatchCardTag(
                                text = "$winnerTeamName win",
                                backgroundColor = getTeamColor(winnerTeamName),
                                textColor = EveryLoLTheme.color.black900
                            )
                        }
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

        Column(
            modifier = Modifier
                .clickable { onPredictionClick() },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "승부 & MVP 투표",
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_double_arrow_right),
                    contentDescription = "투표 화면 이동",
                    tint = EveryLoLTheme.color.grayScale600,
                    modifier = Modifier.size(width = 8.3.dp, height = 7.8.dp)
                )
            }

            PredictionBarByStatus(
                status = item.matchStatus,
                team1Rate = item.team1VoteRate,
                team2Rate = item.team2VoteRate,
                team1Name = item.team1Name,
                team2Name = item.team2Name,
                predictedWinnerTeamName = displayWinnerTeamName
            )

            TeamNameRow(
                team1Name = item.team1Name,
                team2Name = item.team2Name,
                predictedWinnerTeamName = displayWinnerTeamName,
                status = item.matchStatus
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MatchActionButton(
                text = "경기 보러가기",
                enabled = item.matchStatus != MatchStatus.SCHEDULED,
                modifier = Modifier.weight(1f),
                onClick = onWatchClick
            )

            MatchActionButton(
                text = "오픈 톡 입장",
                enabled = true,
                modifier = Modifier.weight(1f),
                onClick = onOpenTalkClick
            )
        }
    }
}

@Composable
private fun MatchCardTag(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EveryLoLTheme.color.grayScale900,
    textColor: Color = EveryLoLTheme.color.grayScale600
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = EveryLoLTheme.typography.body03,
            color = textColor
        )
    }
}

@Composable
private fun TeamNameRow(
    team1Name: String,
    team2Name: String,
    predictedWinnerTeamName: String?,
    status: MatchStatus,
    modifier: Modifier = Modifier
) {
    val team1IsWinner = predictedWinnerTeamName == team1Name
    val team2IsWinner = predictedWinnerTeamName == team2Name

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = team1Name,
            color = getTeamTextColor(
                status = status,
                isWinner = team1IsWinner,
                teamName = team1Name
            ),
            style = EveryLoLTheme.typography.body03
        )

        Text(
            text = team2Name,
            color = getTeamTextColor(
                status = status,
                isWinner = team2IsWinner,
                teamName = team2Name
            ),
            style = EveryLoLTheme.typography.body03
        )
    }
}

@Composable
private fun MatchActionButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(134.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(if (enabled) EveryLoLTheme.color.grayScale400 else EveryLoLTheme.color.gray800)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = EveryLoLTheme.color.black900,
            style = EveryLoLTheme.typography.body03
        )
    }
}

@Composable
private fun PredictionBarByStatus(
    status: MatchStatus,
    team1Rate: Double,
    team2Rate: Double,
    team1Name: String,
    team2Name: String,
    predictedWinnerTeamName: String?,
    modifier: Modifier = Modifier
) {
    val leftWeight = if (team1Rate <= 0.0 && team2Rate <= 0.0) 1f else (team1Rate.coerceAtLeast(0.1) / 100.0).toFloat()
    val rightWeight = if (team1Rate <= 0.0 && team2Rate <= 0.0) 1f else (team2Rate.coerceAtLeast(0.1) / 100.0).toFloat()

    val leftColor = getBarColor(
        status = status,
        isWinner = predictedWinnerTeamName == team1Name,
        teamName = team1Name
    )

    val rightColor = getBarColor(
        status = status,
        isWinner = predictedWinnerTeamName == team2Name,
        teamName = team2Name
    )


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(leftWeight)
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(leftColor)
        )

        Box(
            modifier = Modifier
                .weight(rightWeight)
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(rightColor)
        )
    }
}

@Composable
private fun matchStatusDotColor(status: MatchStatus) = when (status) {
    MatchStatus.SCHEDULED -> EveryLoLTheme.color.grayScale800
    MatchStatus.LIVE -> EveryLoLTheme.color.semanticWarning
    MatchStatus.FINISHED -> EveryLoLTheme.color.grayScale800
}

@Composable
private fun getTeamColor(teamName: String): Color {
    return when (teamName.uppercase()) {
        "DK", "DPLUS KIA" -> EveryLoLTheme.color.teamDK
        "BFX", "BNK FEARX", "FEARX" -> EveryLoLTheme.color.teamBFX
        "HLE", "HANWHA LIFE ESPORTS" -> EveryLoLTheme.color.teamHLE
        "GEN", "GEN.G", "GENG" -> EveryLoLTheme.color.teamGen
        "T1" -> EveryLoLTheme.color.teamT1
        "KT", "KT Rolster", "KT ROLSTER" -> EveryLoLTheme.color.teamKT
        "BRO", "BRION" -> EveryLoLTheme.color.teamBRO
        "DRX", "KRX" -> EveryLoLTheme.color.teamKRX
        "DN", "DNF", "DNS" -> EveryLoLTheme.color.teamDNS
        "NS", "NONGSHIM" -> EveryLoLTheme.color.teamNS
        else -> EveryLoLTheme.color.gray800
    }
}

@Composable
private fun getBarColor(
    status: MatchStatus,
    isWinner: Boolean,
    teamName: String
): Color {
    return when (status) {
        MatchStatus.SCHEDULED -> getTeamColor(teamName)
        MatchStatus.LIVE -> EveryLoLTheme.color.gray800
        MatchStatus.FINISHED -> {
            if (isWinner) getTeamColor(teamName)
            else EveryLoLTheme.color.gray800
        }
    }
}

@Composable
private fun getTeamTextColor(
    status: MatchStatus,
    isWinner: Boolean,
    teamName: String
): Color {
    return when (status) {
        MatchStatus.SCHEDULED -> EveryLoLTheme.color.gray800
        MatchStatus.LIVE -> EveryLoLTheme.color.gray800
        MatchStatus.FINISHED -> {
            if (isWinner) getTeamColor(teamName)
            else EveryLoLTheme.color.gray800
        }
    }
}