package every.lol.com.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.Team
import every.lol.com.feature.mypage.model.MypageUiState

@Composable
fun Predictions(
    prediction: MypageUiState.PredictionItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = prediction.matchDate,
                style = EveryLoLTheme.typography.label03,
                color = EveryLoLTheme.color.white200
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TeamText(
                        modifier = Modifier.padding(bottom=4.dp),
                        teamId = prediction.awayTeamId,
                        isWinner = prediction.homeTeamId == prediction.winnerTeamId,
                        isMyVote = prediction.homeTeamId == prediction.myVoteTeamId
                    )

                    Text(
                        text = "VS",
                        style = EveryLoLTheme.typography.heading02,
                        color = EveryLoLTheme.color.gray800
                    )

                    TeamText(
                        teamId = prediction.homeTeamId,
                        isWinner = prediction.awayTeamId == prediction.winnerTeamId,
                        isMyVote = prediction.awayTeamId == prediction.myVoteTeamId
                    )
                }
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            width = 1.dp,
                            color = if(prediction.myVoteTeamId == prediction.winnerTeamId) EveryLoLTheme.color.semanticCheck else EveryLoLTheme.color.grayScale800,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(EveryLoLTheme.color.grayScale1000)
                        .padding(4.dp),
                    text = if(prediction.myVoteTeamId == prediction.winnerTeamId) "예측 성공" else "예측 실패",
                    style = EveryLoLTheme.typography.label03,
                    color = if(prediction.myVoteTeamId == prediction.winnerTeamId) EveryLoLTheme.color.white200 else EveryLoLTheme.color.gray800
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = EveryLoLTheme.color.grayScale900
            )
        }
    }
}

@Composable
private fun TeamText(
    modifier : Modifier = Modifier,
    teamId: Int,
    isWinner: Boolean = false,
    isMyVote: Boolean = false
){
    val team = Team.fromTeamId(teamId)
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isMyVote) EveryLoLTheme.color.grayScale900 else Color.Transparent)
            .padding(4.dp, 3.dp),
        text = team.teamName,
        style = EveryLoLTheme.typography.heading02,
        color = if (isWinner) EveryLoLTheme.color.white200 else EveryLoLTheme.color.grayScale800
    )
}