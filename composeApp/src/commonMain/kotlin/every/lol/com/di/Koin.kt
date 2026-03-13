package every.lol.com.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import every.lol.com.core.data.di.repositoryModule
import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.data.repository.MyPageRepositoryImpl
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.datastore.AuthPreferences
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.domain.usecase.SignupUseCase
import every.lol.com.core.domain.usecase.SocialLoginUseCase
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.di.dataSourceModule
import every.lol.com.core.network.di.networkModule
import every.lol.com.core.network.remote.AuthDataSourceImpl
import every.lol.com.core.network.remote.MyPagesDataSourceImpl
import every.lol.com.feature.intro.IntroViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appDependenciesModule = module {

    single { AuthPreferences(get<DataStore<Preferences>>()) }
    single { AuthLocalDataSource(get()) }

    single<AuthDataSource> { AuthDataSourceImpl(get(named("noAuth"))) }
    single<MyPagesDataSource> { MyPagesDataSourceImpl(get(named("auth"))) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<MyPagesRepository> { MyPageRepositoryImpl(get(), get()) }

    factory { SocialLoginUseCase(get(), get()) }
    factory { SignupUseCase(get()) }
    factory { NicknameUseCase(get()) }
    factoryOf(::IntroViewModel)
}

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    platformModules: List<Module> = emptyList()
    ) {
    if (GlobalContext.getOrNull() == null) {
        startKoin {
            appDeclaration()
            printLogger(Level.DEBUG)
            modules(
                networkModule,
                dataSourceModule,
                repositoryModule,
                appDependenciesModule,
                *platformModules.toTypedArray()
            )
        }
    }
}