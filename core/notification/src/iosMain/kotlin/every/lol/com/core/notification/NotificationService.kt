package every.lol.com.core.notification

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

class NotificationService : NSObject(), UNUserNotificationCenterDelegateProtocol {

    fun setupPushNotifications() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.delegate = this

        // 권한 요청
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { granted, error ->
            if (granted) {
                println("iOS: Push permission granted")
            }
        }
    }

    // 포그라운드에서 알림을 받았을 때 처리
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
    ) {
        withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
    }
}

/*
Todo: iOS 백그라운드 업로드 핵심 설정 (Ktor Darwin)
iOS에서 550MB를 올리다가 앱을 내렸을 때 전송이 끊기지 않으려면, HttpClient 설정(보통 core:network 모듈)에서 Darwin 엔진을 다음과 같이 설정해야 합니다.

// iosMain의 HttpClient 설정 부분
val client = HttpClient(Darwin) {
    engine {
        configureSession {
            // 이 설정이 있어야 앱이 백그라운드에서도 전송을 포기하지 않습니다.
            val configuration = NSURLSessionConfiguration.backgroundSessionConfigurationWithIdentifier("every.lol.upload")
            configuration.sharedContainerIdentifier = "group.every.lol" // App Group 사용 시
            configuration.sessionSendsLaunchEvents = true
        }
    }
}*/
