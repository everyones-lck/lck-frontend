package every.lol.com.core.notification

interface UploadNotification {
    fun showProgress(progress: Int, fileName: String)
    fun showSuccess(fileName: String)
    fun showError(fileName: String, message: String)
    fun cancel(notificationId: Int)
}