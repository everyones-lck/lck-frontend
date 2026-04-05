package every.lol.com.core.common

import android.content.Context
import android.net.Uri
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.streams.asInput
import java.io.File
import java.util.UUID

// androidMain
actual fun openFileStream(context: Any, uriString: String): Input {
    val androidContext = context as? Context ?: throw Exception("Android Context가 필요합니다.")
    val uri = Uri.parse(uriString)

    return try {
        // 1. ContentResolver를 통해 즉시 InputStream을 엽니다.
        val inputStream = androidContext.contentResolver.openInputStream(uri)
            ?: throw kotlinx.io.files.FileNotFoundException("파일을 찾을 수 없거나 권한이 없습니다: $uriString")

        // 2. java.io.InputStream을 Ktor의 io.ktor.utils.io.core.Input으로 변환
        inputStream.asInput()
    } catch (e: SecurityException) {
        // 3. 만약 여기서 에러가 난다면 이미 시스템이 권한을 회수한 상태입니다.
        throw Exception("URI 접근 권한이 만료되었습니다. 다시 시도해주세요. (${e.message})")
    }
}

actual fun saveCompressedImageToFile(bytes: ByteArray): String {
    val tempFile = File.createTempFile("compressed_${UUID.randomUUID()}", ".jpg")
    tempFile.writeBytes(bytes)
    return tempFile.absolutePath
}