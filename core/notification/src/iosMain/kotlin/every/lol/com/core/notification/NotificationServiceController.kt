package every.lol.com.core.notification

class IosNotificationServiceController : NotificationServiceController {

    override fun startService(fileName: String) {
        // iOS는 별도의 Foreground Service가 없음.
        // 대신 Ktor Darwin 엔진이 NSURLSessionConfiguration.backgroundSessionConfiguration을
        // 사용하도록 설정되어 있어야 백그라운드 전송이 유지됩니다.
        println("iOS: Background upload session initiated for $fileName")
    }

    override fun stopService() {
        // 전송 완료 후 정리 로직이 필요하다면 여기서 수행
        println("iOS: Background upload session finished")
    }
}