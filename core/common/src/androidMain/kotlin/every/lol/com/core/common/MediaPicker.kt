package every.lol.com.core.common

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberMultiResourcePickerLauncher(
    onResult: (List<Any>) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(12)
    ) { uris ->
        onResult(uris)
    }

    return {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        )
    }
}

actual suspend fun getMediaMetadata(
    context: Any,
    uriString: String
): VideoMetadata {
    val androidContext = context as? Context ?: return VideoMetadata(0L, null)
    val retriever = MediaMetadataRetriever()

    return try {
        val uri = Uri.parse(uriString)
        // 💡 넘겨받은 context를 직접 사용
        retriever.setDataSource(androidContext, uri)

        val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L

        val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        val thumbnailBytes = bitmap?.let {
            val stream = ByteArrayOutputStream()
            val scaled = Bitmap.createScaledBitmap(it, 320, 180, true)
            scaled.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            stream.toByteArray()
        }

        VideoMetadata(durationMs, thumbnailBytes)
    } catch (e: Exception) {
        VideoMetadata(0L, null)
    } finally {
        retriever.release()
    }
}