package every.lol.com.feature.intro.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import everylol.feature.intro.generated.resources.Res
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.intro.generated.resources.ic_camera
import everylol.feature.intro.generated.resources.img_default_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun Profile(
    onOpenGallery:(()-> Unit)?=null,
    profile: Any?=null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(112.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(105.dp)
                .clip(CircleShape)
                .background(EveryLoLTheme.color.grayScale200)
        ) {
            if (profile == null) {
                Image(
                    painter = painterResource(Res.drawable.img_default_profile),
                    contentDescription = "Default Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = profile,
                    contentDescription = "User Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (onOpenGallery != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onOpenGallery() }
            ) {
                OpenGalleryButton()
            }
        }
    }
}

@Composable
fun OpenGalleryButton(){
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ){
        Icon(
            painter = painterResource(Res.drawable.ic_camera),
            tint = EveryLoLTheme.color.grayScale200,
            contentDescription = "Open Gallery",
            modifier = Modifier.size(20.dp)
        )
    }
}