package every.lol.com.core.common

import android.net.Uri


actual fun getFileSize(context: Any, uriString: String): Long {
    val contentResolver = (context as android.content.Context).contentResolver
    val uri = Uri.parse(uriString)

    return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
        cursor.moveToFirst()
        cursor.getLong(sizeIndex)
    } ?: 0L
}