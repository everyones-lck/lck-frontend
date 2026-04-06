package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun EverylolToast (
    text: String,
    modifier : Modifier = Modifier
){
    val borderColor = EveryLoLTheme.color.grayScale800
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000.copy(0.8f))
            .drawWithContent {
                drawContent()
                val strokeWidth = 1.dp.toPx()
                val halfStrokeWidth = strokeWidth / 2

                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(halfStrokeWidth, halfStrokeWidth),
                    size = Size(
                        size.width - strokeWidth,
                        size.height - strokeWidth
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
            .padding(24.dp, 12.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = text,
            style = EveryLoLTheme.typography.pretendardBody01,
            color = EveryLoLTheme.color.white200
        )
    }
}