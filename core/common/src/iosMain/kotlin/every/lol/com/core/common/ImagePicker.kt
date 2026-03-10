package every.lol.com.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSUUID
import platform.PhotosUI.*
import platform.UIKit.*
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

private const val OPEN_IMAGE_PICKER_NOTIFICATION = "EverylolOpenImagePicker"
private const val IMAGE_PICKER_RESULT_NOTIFICATION = "EverylolImagePickerResult"
private const val KEY_REQUEST_ID = "requestId"
private const val KEY_IMAGE_PATH = "imagePath"

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(
    onResult: (Any?) -> Unit
): () -> Unit {
    val currentOnResult by rememberUpdatedState(onResult)
    val requestId = remember { NSUUID().UUIDString() }

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = IMAGE_PICKER_RESULT_NOTIFICATION,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { notification ->
            val userInfo = notification?.userInfo ?: return@addObserverForName
            val resultRequestId = userInfo[KEY_REQUEST_ID] as? String ?: return@addObserverForName
            if (resultRequestId != requestId) return@addObserverForName

            val imagePath = userInfo[KEY_IMAGE_PATH] as? String
            currentOnResult(imagePath)
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    return {
        NSNotificationCenter.defaultCenter.postNotificationName(
            OPEN_IMAGE_PICKER_NOTIFICATION,
            null,
            mapOf(KEY_REQUEST_ID to requestId)
        )
    }
}