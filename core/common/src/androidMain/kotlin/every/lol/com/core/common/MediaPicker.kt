package every.lol.com.core.common

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

actual fun isVideoUri(uri: Any, context: Any): Boolean {
    val androidContext = context as? Context ?: return false
    val androidUri = (uri as? Uri) ?: Uri.parse(uri.toString())
    val mimeType = androidContext.contentResolver.getType(androidUri)
    return mimeType?.startsWith("video") == true
}

@Composable
actual fun rememberVideoThumbnail(videoUrl: String): ImageBitmap? {
    var bitmap by remember(videoUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(videoUrl) {
        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {

                retriever.setDataSource(videoUrl, HashMap<String, String>())

                val frame = retriever.getFrameAtTime(
                    1000000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )

                bitmap = frame?.asImageBitmap()
            } catch (e: Exception) {
                println("Ktor Log: Thumbnail Extraction Failed - ${e.message}")
            } finally {
                try {
                    retriever.release()
                } catch (e: Exception) { /* 무시 */ }
            }
        }
    }
    return bitmap
}