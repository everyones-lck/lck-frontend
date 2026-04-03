package every.lol.com.core.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberMultiResourcePickerLauncher(
    onResult: (List<Any>) -> Unit
): () -> Unit

data class VideoMetadata(
    val durationMs: Long,
    val thumbnail: ByteArray?
)

expect suspend fun getMediaMetadata(
    context: Any,
    uriString: String
): VideoMetadata