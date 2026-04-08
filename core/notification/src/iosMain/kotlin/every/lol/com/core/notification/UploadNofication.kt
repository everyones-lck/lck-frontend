package every.lol.com.core.notification

import platform.UserNotifications.*

class IosUploadNotifier : UploadNotification {
    override fun showProgress(progress: Int, fileName: String) {
        // 로그를 남기거나 내부 UI Progress만 업데이트
    }

    override fun showSuccess(fileName: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle("업로드 완료")
            setBody("$fileName 게시물이 업로드되었습니다.")
        }
        val request = UNNotificationRequest.create("id_success", content, null)
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }

    override fun showError(fileName: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle("업로드 실패")
            setBody("$fileName: $message")
        }
        val request = UNNotificationRequest.create("id_error", content, null)
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }

    override fun cancel(notificationId: Int) { /* iOS cancel logic */ }
}