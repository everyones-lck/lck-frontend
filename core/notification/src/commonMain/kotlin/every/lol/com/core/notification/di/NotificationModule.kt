package every.lol.com.core.notification.di

import org.koin.core.module.Module
import org.koin.dsl.module


val notificationModule = module {

    includes(platformNotifierModule)
}

expect val platformNotifierModule: Module