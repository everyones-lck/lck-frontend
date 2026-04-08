package every.lol.com.core.notification

interface NotificationServiceController {
    fun startService(fileName: String)
    fun stopService()
}