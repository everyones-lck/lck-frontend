package every.lol.com.core.notification.di

import every.lol.com.core.notification.AndroidNotificationServiceController
import every.lol.com.core.notification.AndroidUploadNotification
import every.lol.com.core.notification.NotificationServiceController
import every.lol.com.core.notification.UploadNotification
import org.koin.dsl.module


actual val platformNotifierModule = module {
    single<UploadNotification> { AndroidUploadNotification(get()) }
    single<NotificationServiceController> { AndroidNotificationServiceController(get()) }
}