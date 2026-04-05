package every.lol.com.feature.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import every.lol.com.core.common.rememberVideoThumbnail
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_player
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommunityVideo(
    videoUrl: String,
    onVideoClick: (String) -> Unit
) {
    val thumbnail = rememberVideoThumbnail(videoUrl)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable { onVideoClick(videoUrl) },
        contentAlignment = Alignment.Center
    ) {
        if (thumbnail != null) {
            Image(
                bitmap = thumbnail,
                contentDescription = "Video Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = EveryLoLTheme.color.grayScale200,
                strokeWidth = 2.dp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Icon(
            painter = painterResource(Res.drawable.ic_player),
            contentDescription = "Play Video",
            modifier = Modifier.size(40.dp),
            tint = Color.White
        )
    }
}