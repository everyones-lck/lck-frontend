package every.lol.com.core.network.di

import every.lol.com.core.network.datasource.SocialLoginDataSource
import org.koin.dsl.module

actual val platformDataSourceModule = module {
    single { SocialLoginDataSource(get()) }
}