package every.lol.com.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import every.lol.com.core.model.Team

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamChip(
    team: Team,
    isSelected: Boolean = false,
    isSelectable: Boolean = true,
    onClick: () -> Unit = {}
){
    val displaySelected = if (isSelectable) isSelected else true
    val teamChipColor = if (displaySelected) EveryLoLTheme.color.grayScale200 else EveryLoLTheme.color.grayScale800

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(52.dp))
            .then(
                if (isSelectable) {
                    Modifier.combinedClickable(
                        onClick = onClick,
                        onLongClick = onClick
                    )
                } else Modifier
            )
            .border(1.dp,
                color = teamChipColor,
                shape = RoundedCornerShape(52.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if (isSelectable) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(team.getTeamBrush())
            )
        }
        Text(
            text = team.teamName,
            style = EveryLoLTheme.typography.body03,
            color = teamChipColor
        )
    }
}

@Composable
fun Team.getTeamBrush(): Brush {
    return when (this.id) {
        1 -> SolidColor(EveryLoLTheme.color.teamT1)
        2 -> SolidColor(EveryLoLTheme.color.teamGen)
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