package every.lol.com.feature.matches.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.MatchStatus

enum class PredictionTeam {
    TEAM1, TEAM2
}

enum class PredictionResult {
    SUCCESS, FAIL
}

@Composable
fun PredictionVoteSection(
    title: String,
    matchStatus: MatchStatus,
    team1Name: String,
    team2Name: String,
    selectedTeam: PredictionTeam?,
    predictionResult: PredictionResult? = null,
    isVoteEnabled: Boolean,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusText = when (matchStatus) {
        MatchStatus.SCHEDULED -> null
        MatchStatus.LIVE -> "경기중"
        MatchStatus.FINISHED -> when (predictionResult) {
            PredictionResult.SUCCESS -> "예측 성공"
            PredictionResult.FAIL -> "예측 실패"
            null -> null
        }
    }

    val statusTextColor = when (matchStatus) {
        MatchStatus.SCHEDULED -> EveryLoLTheme.color.grayScale200
        MatchStatus.LIVE -> EveryLoLTheme.color.grayScale200
        MatchStatus.FINISHED -> when (predictionResult) {
            PredictionResult.SUCCESS -> EveryLoLTheme.color.semanticCheck
            PredictionResult.FAIL -> EveryLoLTheme.color.semanticWarning
            null -> EveryLoLTheme.color.grayScale200
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = EveryLoLTheme.color.grayScale600,
                style = EveryLoLTheme.typography.subtitle03
            )

            if (statusText != null) {
                Text(
                    text = statusText,
                    color = statusTextColor,
                    style = EveryLoLTheme.typography.subtitle03
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PredictionTeamButton(
                text = team1Name,
                team = PredictionTeam.TEAM1,
                matchStatus = matchStatus,
                selectedTeam = selectedTeam,
                predictionResult = predictionResult,
                isVoteEnabled = isVoteEnabled,
                modifier = Modifier.weight(1f),
                onClick = onLeftClick
            )


            PredictionTeamButton(
                text = team2Name,
                team = PredictionTeam.TEAM2,
                matchStatus = matchStatus,
                selectedTeam = selectedTeam,
                predictionResult = predictionResult,
                isVoteEnabled = isVoteEnabled,
                modifier = Modifier.weight(1f),
                onClick = onRightClick
            )
        }
    }
}

@Composable
private fun PredictionTeamButton(
    text: String,
    team: PredictionTeam,
    matchStatus: MatchStatus,
    selectedTeam: PredictionTeam?,
    predictionResult: PredictionResult?,
    isVoteEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedTeam == team
    val teamColor = getTeamColor(text)

    val backgroundColor = when (matchStatus) {
        MatchStatus.SCHEDULED -> {
            if (isSelected) EveryLoLTheme.color.grayScale400
            else EveryLoLTheme.color.grayScale900
        }

        MatchStatus.LIVE -> {
            if (isSelected) teamColor
            else EveryLoLTheme.color.grayScale900
        }

        MatchStatus.FINISHED -> {
            if (isSelected) {
                when (predictionResult) {
                    PredictionResult.SUCCESS -> teamColor
                    PredictionResult.FAIL -> EveryLoLTheme.color.grayScale400
                    null -> EveryLoLTheme.color.grayScale400
                }
            } else {
                EveryLoLTheme.color.grayScale900
            }
        }
    }

    val textColor = when (matchStatus) {
        MatchStatus.SCHEDULED -> {
            if (isSelected) EveryLoLTheme.color.black900
            else EveryLoLTheme.color.grayScale700
        }

        MatchStatus.LIVE -> {
            if (isSelected) EveryLoLTheme.color.black900
            else EveryLoLTheme.color.grayScale700
        }

        MatchStatus.FINISHED -> {
            if (isSelected) EveryLoLTheme.color.black900
            else EveryLoLTheme.color.grayScale700
        }
    }

    val enabled = isVoteEnabled

    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = EveryLoLTheme.typography.body01
        )
    }
}

@Composable
private fun getTeamColor(teamName: String): Color {
    return when (teamName.uppercase()) {
        "DK", "DPLUS KIA" -> EveryLoLTheme.color.teamDK
        "BFX", "BNK FEARX", "FEARX" -> EveryLoLTheme.color.teamBFX
        "HLE", "HANWHA LIFE ESPORTS" -> EveryLoLTheme.color.teamHLE
        "GEN", "GEN.G", "GENG" -> EveryLoLTheme.color.teamGen
        "T1" -> EveryLoLTheme.color.teamT1
        "KT", "KT Rolster" -> EveryLoLTheme.color.teamKT
        "BRO", "BRION" -> EveryLoLTheme.color.teamBRO
        "DRX", "KRX" -> EveryLoLTheme.color.teamKRX
        "DN", "DNF", "DNS" -> EveryLoLTheme.color.teamDNS
        "NS", "NONGSHIM" -> EveryLoLTheme.color.teamNS
        else -> EveryLoLTheme.color.gray800
    }
}