package every.lol.com.core.common

import android.content.Context
import android.net.Uri
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.streams.asInput
import java.io.File
import java.util.UUID

// androidMain
actual fun openFileStream(context: Any, path: String): Input {
    val androidContext = context as Context
    val uri = Uri.parse(path)
    val inputStream = androidContext.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Could not open stream for $path")
    return inputStream.asInput()
}

actual fun saveCompressedImageToFile(bytes: ByteArray): String {
    val tempFile = File.createTempFile("compressed_${UUID.randomUUID()}", ".jpg")
    tempFile.writeBytes(bytes)
    return tempFile.absolutePath
}