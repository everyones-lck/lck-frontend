package every.lol.com.core.common

import io.ktor.utils.io.core.Input

expect fun openFileStream(context: Any, path: String): Input

expect fun saveCompressedImageToFile(bytes: ByteArray): String