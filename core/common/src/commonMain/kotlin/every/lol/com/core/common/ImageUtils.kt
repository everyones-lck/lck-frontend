package every.lol.com.core.common

expect fun Any?.toImageByteArray(): ByteArray?

expect fun ByteArray.compressImage(quality: Int = 70): ByteArray