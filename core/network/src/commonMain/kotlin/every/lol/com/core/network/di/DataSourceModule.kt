package every.lol.com.core.network.di

import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.remote.AuthDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module


val dataSourceModule = module {

    single<AuthDataSource> { AuthDataSourceImpl(get()) }

    includes(platformDataSourceModule)
}

expect val platformDataSourceModule: Module