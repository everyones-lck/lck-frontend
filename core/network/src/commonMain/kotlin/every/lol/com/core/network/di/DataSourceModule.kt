package every.lol.com.core.network.di

import every.lol.com.core.network.datasource.AboutLCKDataSource
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.remote.AboutLCKDataSourceImpl
import every.lol.com.core.network.remote.AuthDataSourceImpl
import every.lol.com.core.network.remote.CommunityDataSourceImpl
import every.lol.com.core.network.remote.MyPagesDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module


val dataSourceModule = module {

    single<AuthDataSource> { AuthDataSourceImpl(get()) }
    single<MyPagesDataSource> { MyPagesDataSourceImpl(get()) }
    single<CommunityDataSource> { CommunityDataSourceImpl(get()) }
    single<AboutLCKDataSource> { AboutLCKDataSourceImpl(get()) }

    includes(platformDataSourceModule)
}

expect val platformDataSourceModule: Module