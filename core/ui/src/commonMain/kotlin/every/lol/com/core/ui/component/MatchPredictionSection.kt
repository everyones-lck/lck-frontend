package every.lol.com.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.ic_double_arrow_right
import org.jetbrains.compose.resources.painterResource

@Composable
fun MatchPredicionSection(
    data: MatchCardModel,
    title: String,
    onPredictionClick: (() -> Unit)? = null,
) {


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.clickable(
                enabled = onPredictionClick != null,
                onClick = { onPredictionClick?.invoke() }
            )
        ) {
            Text(
                text = title,
                color = if(title == "승부예측결과")EveryLoLTheme.color.gray800 else EveryLoLTheme.color.grayScale600,
                style = EveryLoLTheme.typography.subtitle03
            )
            if(title == "승부 & MVP 투표") {
                Icon(
                    painter = painterResource(Res.drawable.ic_double_arrow_right),
                    contentDescription = "투표 화면 이동",
                    tint = EveryLoLTheme.color.grayScale600,
                    modifier = Modifier.size(width = 8.3.dp, height = 7.8.dp)
                )
            }
        }

        PredictionBarByStatus(
            status = data.matchStatus,
            team1Rate = data.team1VoteRate,
            team2Rate = data.team2VoteRate,
            team1Name = data.team1Name,
            team2Name = data.team2Name,
            predictedWinnerTeamName = data.predictedWinnerTeamName
        )

        TeamNameRow(
            team1Name = data.team1Name,
            team2Name = data.team2Name,
            predictedWinnerTeamName = data.predictedWinnerTeamName,
            status = data.matchStatus
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
            color = if(team1IsWinner)getTeamColor(team1Name) else EveryLoLTheme.color.grayScale800,
            style = EveryLoLTheme.typography.body03
        )

        Text(
            text = team2Name,
            color = if(team2IsWinner)getTeamColor(team2Name) else EveryLoLTheme.color.grayScale800,
            style = EveryLoLTheme.typography.body03
        )
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