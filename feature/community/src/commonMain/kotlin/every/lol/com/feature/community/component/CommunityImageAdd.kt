package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_picture
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommunityImageAdd(
    modifier: Modifier = Modifier,
    openGallery: () -> Unit
){

    val borderColor = EveryLoLTheme.color.grayScale900

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EveryLoLTheme.color.grayScale1000)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }
            .padding(24.dp, 12.dp),
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            modifier = Modifier.size(20.dp)
                .clickable{
                    openGallery()
                },
            painter = painterResource(Res.drawable.ic_picture),
            contentDescription = null,
            tint = EveryLoLTheme.color.white200,
        )
    }
}
