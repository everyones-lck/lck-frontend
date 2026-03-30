package every.lol.com.core.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.ByteArrayOutputStream

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

actual fun ByteArray.compressImage(quality: Int): ByteArray {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size) ?: return this
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}