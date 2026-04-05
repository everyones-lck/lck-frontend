package every.lol.com.core.common

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import platform.AVFoundation.*
import platform.CoreMedia.*
import platform.Foundation.*
import platform.UIKit.*
import org.jetbrains.skia.Image


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
    // 런처가 호출되었을 때 실행할 빈 람다 반환
    return {
        // iOS 구현 전까지 비워둠
    }
}
/*
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
      *//*  NSNotificationCenter.defaultCenter.postNotificationName(
            name = OPEN_IMAGE_PICKER_NOTIFICATION,
            `object` = null,
            userInfo = mapOf(
                KEY_REQUEST_ID to requestId,
                "maxImageCount" to 10,
                "maxVideoCount" to 2,
                "videoDurationLimit" to 180.0
            )
        )*//*
    }
}
*/

//동영상에서 썸네일 가져오는 함수
@OptIn(ExperimentalForeignApi::class)
actual suspend fun getMediaMetadata(context: Any, uriString: String): VideoMetadata {
    // iOS 구현 전까지 기본값(0초, 썸네일 없음) 반환
    return VideoMetadata(0L, null)
}
/*

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getMediaMetadata(context: Any, uriString: String): VideoMetadata {
    val url = NSURL.URLWithString(uriString) ?: return VideoMetadata(0L, null)
    val asset = AVURLAsset.assetWithURL(url) as AVAsset

    val duration = asset.duration
    val durationMs = (CMTimeGetSeconds(duration) * 1000).toLong()

    val imageGenerator = AVAssetImageGenerator(asset = asset).apply {
        appliesPreferredTrackTransform = true
    }

    return try {
        val cgImage = imageGenerator.copyCGImageAtTime(
            requestedTime = CMTimeMake(0, 1),
            actualTime = null,
            error = null
        ) ?: return VideoMetadata(durationMs, null)

        val uiImage = UIImage.imageWithCGImage(cgImage)

        val data = UIImageJPEGRepresentation(uiImage, 0.7)
        val thumbnailBytes = data?.toByteArray()

        VideoMetadata(durationMs, thumbnailBytes)
    } catch (e: Exception) {
        VideoMetadata(durationMs, null)
    }
}
*/

actual fun isVideoUri(uri: Any, context: Any): Boolean {
    // iOS는 보통 URI 문자열에 확장자가 포함되거나 PHAsset 타입을 사용함
    val uriString = uri.toString().lowercase()
    return uriString.contains(".mp4") || uriString.contains(".mov")
}

@Composable
actual fun rememberVideoThumbnail(videoUrl: String): ImageBitmap? {
    var bitmap by remember(videoUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(videoUrl) {
        val url = NSURL.URLWithString(videoUrl) ?: return@LaunchedEffect
        val asset = AVURLAsset.assetWithURL(url)
        val generator = AVAssetImageGenerator(asset = asset).apply {
            appliesPreferredTrackTransform = true // 영상 회전값 자동 보정
        }

        val time = CMTimeMake(value = 1, timescale = 1) // 1초 지점

        try {
            // iOS 네이티브 비동기 추출 로직 (CGImage를 ImageBitmap으로 변환하는 과정 필요)
            // 실제 프로젝트의 Skia 유틸리티 함수(asImageBitmap)를 사용하여 변환하세요.
            // bitmap = cgImage.toComposeImageBitmap()
        } catch (e: Exception) {
            println("iOS Thumbnail Error: ${e.message}")
        }
    }
    return bitmap
}