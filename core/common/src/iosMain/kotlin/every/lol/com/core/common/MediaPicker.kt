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

private const val OPEN_IMAGE_PICKER_NOTIFICATION = "EverylolOpenImagePicker"
private const val IMAGE_PICKER_RESULT_NOTIFICATION = "EverylolImagePickerResult"
private const val KEY_REQUEST_ID = "requestId"
private const val KEY_MEDIA_LIST = "mediaList"
private const val KEY_MEDIA_TYPE = "type" // "image" or "video"
private const val KEY_MEDIA_PATH = "path"
private const val KEY_VIDEO_DURATION = "duration" // 초 단위 (Double)

//Todo: iOS 전용 미디어 가져오기 만들기
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberMultiResourcePickerLauncher(
    onResult: (List<Any>) -> Unit
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

            // Swift에서 넘어온 미디어 리스트를 파싱합니다.
            val rawMediaList = userInfo[KEY_MEDIA_LIST] as? List<Map<String, Any>> ?: return@addObserverForName

            val filteredList = mutableListOf<String>()
            var imageCount = 0
            var videoCount = 0

            rawMediaList.forEach { media ->
                val type = media[KEY_MEDIA_TYPE] as? String
                val path = media[KEY_MEDIA_PATH] as? String ?: return@forEach

                if (type == "video") {
                    val duration = (media[KEY_VIDEO_DURATION] as? Double) ?: 0.0
                    // 동영상 제한: 최대 2개, 3분(180초) 이내
                    if (videoCount < 2 && duration <= 180.0) {
                        filteredList.add(path)
                        videoCount++
                    }
                } else {
                    // 이미지 제한: 최대 10개
                    if (imageCount < 10) {
                        filteredList.add(path)
                        imageCount++
                    }
                }
            }

            // 최종 필터링된 경로 리스트를 보냅니다.
            currentOnResult(filteredList)
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    return {
        NSNotificationCenter.defaultCenter.postNotificationName(
            name = OPEN_IMAGE_PICKER_NOTIFICATION,
            `object` = null,
            // Swift 쪽에서 이 파라미터들을 참고해서 PHPicker를 띄워야 합니다.
            userInfo = mapOf(
                KEY_REQUEST_ID to requestId,
                "maxImageCount" to 10,
                "maxVideoCount" to 2,
                "videoDurationLimit" to 180.0
            )
        )
    }
}