package every.lol.com.core.notification

interface RemotePushHandler {
    fun handleRemoteMessage(title: String, body: String, data: Map<String, String>)
    fun requestPermission()
}