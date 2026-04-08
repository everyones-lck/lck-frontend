package every.lol.com.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat

class AndroidUploadNotification(private val context: Context) : UploadNotification {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "upload_channel"
    private val notificationId = 1001

    init {
        if (SDK_INT >= O) {
            val channel = NotificationChannel(channelId, "Uploads", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showProgress(progress: Int, fileName: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setContentTitle("게시글이 업로드 중입니다")
            .setContentText("잠시만 기다려 주세요... ($progress%)")
            .setProgress(100, progress, false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    override fun showSuccess(fileName: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)
            .setContentTitle(fileName)
            .setContentText("업로드 완료!")
            .setOngoing(false)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    override fun showError(fileName: String, message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("업로드 실패: $fileName")
            .setContentText(message)
            .setOngoing(false)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    override fun cancel(notificationId: Int) = notificationManager.cancel(notificationId)
}