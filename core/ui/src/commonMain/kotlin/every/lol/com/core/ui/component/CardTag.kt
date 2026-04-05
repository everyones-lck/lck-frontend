package every.lol.com.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun CardTag(
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
fun getTeamColor(teamName: String): Color {
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