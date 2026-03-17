package every.lol.com.core.common

import android.content.Context
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object KoinHelper : KoinComponent {
    val context: Context by inject()
}

actual fun Any?.toImageByteArray(): ByteArray? {
    val uri = when (this) {
        is Uri -> this
        is String -> Uri.parse(this)
        else -> return null
    }

    return try {
        val inputStream = KoinHelper.context.contentResolver.openInputStream(uri)
        inputStream?.use { it.readBytes() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}