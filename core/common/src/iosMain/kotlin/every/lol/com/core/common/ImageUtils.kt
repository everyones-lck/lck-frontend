package every.lol.com.core.common

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy
import platform.Foundation.create

actual fun Any?.toImageByteArray(): ByteArray? {
    val data: NSData = when (this) {
        is ByteArray -> return this
        is NSData -> this
        is UIImage -> UIImagePNGRepresentation(this)
            ?: UIImageJPEGRepresentation(this, 0.9)
            ?: return null

        is String -> NSData.dataWithContentsOfFile(this) ?: return null
        else -> return null
    }

    return data.toByteArray()
}

actual fun ByteArray.compressImage(quality: Int): ByteArray {
    val data = this.toNSData()
    val image = UIImage.imageWithData(data) ?: return this
    val compressedData = UIImageJPEGRepresentation(image, quality / 100.0) ?: return this
    return compressedData.toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)

    return ByteArray(size).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()

    return usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = size.toULong()
        )
    }
}