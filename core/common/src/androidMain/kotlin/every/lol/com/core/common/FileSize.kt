package every.lol.com.core.common

import android.net.Uri
import java.io.File


actual fun getFileSize(context: Any, uriString: String): Long {
    val uri = Uri.parse(uriString)

    if (uri.scheme == "file" || uri.scheme == null) {
        val path = uri.path ?: uriString
        val file = File(path)
        return if (file.exists()) file.length() else 0L
    }

    val contentResolver = (context as android.content.Context).contentResolver
    return try {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            if (sizeIndex != -1 && cursor.moveToFirst()) {
                cursor.getLong(sizeIndex)
            } else {
                0L
            }
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}