package every.lol.com.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.ic_headset
import everylol.core.ui.generated.resources.ic_no_content
import everylol.core.ui.generated.resources.ic_unfinished
import org.jetbrains.compose.resources.painterResource

@Composable
fun EmptyContent(
    icon: String? = null,
    message: String = "",
    modifier: Modifier = Modifier
){

    val iconPainter = when (icon) {
        "headset" -> painterResource(Res.drawable.ic_headset)
        "unfinished" -> painterResource(Res.drawable.ic_unfinished)
        "no_content" -> painterResource(Res.drawable.ic_no_content)
        else -> null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(284.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ){
        if(icon!=null) {
            Icon(
                painter = painterResource(Res.drawable.ic_headset),
                contentDescription = null,
                modifier = Modifier.width(58.dp).height(60.dp),
                tint = EveryLoLTheme.color.community600
            )
        }
        Text(
            text = message,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.community600
        )
    }
}