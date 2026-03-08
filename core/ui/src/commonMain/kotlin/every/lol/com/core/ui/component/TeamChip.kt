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
    onClick: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(52.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onClick
            )
            .border(1.dp,
                color = if(isSelected)EveryLoLTheme.color.grayScale200 else EveryLoLTheme.color.grayScale800,
                shape = RoundedCornerShape(52.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(team.getTeamBrush())
        )
        Text(
            text = team.teamName,
            style = EveryLoLTheme.typography.body03,
            color = if(isSelected)EveryLoLTheme.color.grayScale200 else EveryLoLTheme.color.grayScale800
        )
    }
}

@Composable
fun Team.getTeamBrush(): Brush {
    return when (this.id) {
        1 -> SolidColor(EveryLoLTheme.color.teamHLE) // HLE
        2 -> SolidColor(EveryLoLTheme.color.teamGen) // GEN
        3 -> SolidColor(EveryLoLTheme.color.teamT1KT) // T1
        4 -> SolidColor(EveryLoLTheme.color.teamDRXDNF) // DNS
        5 -> SolidColor(EveryLoLTheme.color.teamBFX) // BFX
        6 -> EveryLoLTheme.color.teamNS // NS
        7 -> SolidColor(EveryLoLTheme.color.teamDK) // DK
        8 -> SolidColor(EveryLoLTheme.color.teamDRXDNF) // DRX
        9 -> SolidColor(EveryLoLTheme.color.teamBrion) // BRO
        10 -> SolidColor(EveryLoLTheme.color.teamT1KT) // Kt
        else -> SolidColor(Color.Transparent)
    }
}