package every.lol.com.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.ic_camera
import everylol.core.ui.generated.resources.img_default_profile
import everylol.core.ui.generated.resources.img_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileImage(
    requiredGallery: Boolean?=false,
    onOpenGallery:(()-> Unit)?=null,
    profile: Any?=null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(115.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = profile,
            contentDescription = null,
            modifier = Modifier
                .size(105.dp)
                .clip(CircleShape)
                .background(EveryLoLTheme.color.grayScale900)
                .clickable(enabled = onOpenGallery != null) { onOpenGallery?.invoke() },
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            placeholder = painterResource(Res.drawable.img_default_profile),
            error = painterResource(Res.drawable.img_example),
            fallback = painterResource(Res.drawable.img_example),
            onLoading = {
                println("이미지 로딩 중...")
            },
            onSuccess = { success ->
                println("이미지 로드 성공: ${success.result.dataSource}")
            },
            onError = { error ->
                println("이미지 로드 에러: ${error.result.throwable}")
            }
        )

        if (requiredGallery == true) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(EveryLoLTheme.color.grayScale1000)
                    .clickable (
                        enabled = onOpenGallery != null,
                        onClick = { onOpenGallery?.invoke() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_camera),
                    tint = EveryLoLTheme.color.grayScale200,
                    contentDescription = "Open Gallery",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}