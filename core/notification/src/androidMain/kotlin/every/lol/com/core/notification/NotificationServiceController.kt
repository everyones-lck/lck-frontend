package every.lol.com.core.notification

import android.content.Context
import android.content.Intent

class AndroidNotificationServiceController(private val context: Context) : NotificationServiceController {
    override fun startService(fileName: String) {
        val intent = Intent(context, NotificationService::class.java).apply {
            putExtra("FILE_NAME", fileName)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    override fun stopService() {
        context.stopService(Intent(context, NotificationService::class.java))
    }
}