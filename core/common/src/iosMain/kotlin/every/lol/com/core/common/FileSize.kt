package every.lol.com.core.common

actual fun getFileSize(context: Any, uriString: String): Long {
    val fileManager = platform.Foundation.NSFileManager.defaultManager
    val attributes = fileManager.attributesOfItemAtPath(uriString, null)
    return attributes?.get(platform.Foundation.NSFileSize) as? Long ?: 0L
}