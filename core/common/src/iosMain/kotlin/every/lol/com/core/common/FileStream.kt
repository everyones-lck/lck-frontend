package every.lol.com.core.common

import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun openFileStream(context: Any, path: String): Input {
    val url = NSURL.URLWithString(path) ?: throw Exception("Invalid Path")
    val inputStream = NSInputStream.inputStreamWithURL(url) ?: throw Exception("Stream Open Failed")
    return inputStream.asInput() // Ktor의 ios용 확장함수
}
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        byteArray.usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }
    return byteArray
}

actual fun saveCompressedImageToFile(bytes: ByteArray): String {
    val fileManager = NSFileManager.defaultManager
    val cacheDir = fileManager.URLsForDirectory(NSCachesDirectory, NSUserDomainMask).first() as NSURL
    val fileName = "compressed_${NSUUID().UUIDString()}.jpg"
    val fileURL = cacheDir.URLByAppendingPathComponent(fileName)!!

    val data = bytes.toNSData()
    data.writeToURL(fileURL, true)

    return fileURL.path!!
}