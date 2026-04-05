package every.lol.com.core.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberMultiResourcePickerLauncher(
    onResult:(List<Any>) -> Unit
): () -> Unit

data class VideoMetadata(
    val durationMs: Long,
    val thumbnail: ByteArray?
)

expect suspend fun getMediaMetadata(
    context: Any,
    uriString: String
): VideoMetadata

expect fun isVideoUri(uri: Any, context: Any): Boolean

@Composable
expect fun rememberVideoThumbnail(videoUrl: String): ImageBitmap?