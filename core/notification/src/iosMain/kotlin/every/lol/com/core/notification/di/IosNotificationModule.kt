package every.lol.com.core.notification.di

import every.lol.com.core.notification.IosNotificationServiceController
import every.lol.com.core.notification.IosUploadNotifier
import every.lol.com.core.notification.NotificationServiceController
import every.lol.com.core.notification.UploadNotification
import org.koin.dsl.module


actual val platformNotifierModule = module {
    single<UploadNotification> { IosUploadNotifier() }
    single<NotificationServiceController> { IosNotificationServiceController() }
}