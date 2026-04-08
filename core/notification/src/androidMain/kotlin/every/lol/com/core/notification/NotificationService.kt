package every.lol.com.core.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fileName = intent?.getStringExtra("FILE_NAME") ?: "파일"

        // 서비스를 포그라운드로 전환 (안드로이드 시스템 보호 시작)
        // ID 1001은 AndroidUploadNotifier에서 사용하는 ID와 일치시켜야 알림이 겹치지 않습니다.
        startForeground(1001, createInitialNotification(fileName))

        // START_NOT_STICKY: 시스템에 의해 죽었을 때 자동으로 재시작하지 않음 (대용량 업로드는 재시작보다 에러 처리가 나음)
        return START_NOT_STICKY
    }

    private fun createInitialNotification(fileName: String) =
        NotificationCompat.Builder(this, "upload_channel")
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setContentTitle(fileName)
            .setContentText("업로드를 준비 중입니다...")
            .setOngoing(true)
            .build()

    override fun onBind(intent: Intent?): IBinder? = null
}