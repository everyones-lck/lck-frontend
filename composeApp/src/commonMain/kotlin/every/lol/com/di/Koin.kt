package every.lol.com.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import every.lol.com.core.data.di.repositoryModule
import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.datastore.AuthPreferences
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.usecase.LoginUseCase
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.di.dataSourceModule
import every.lol.com.core.network.di.networkModule
import every.lol.com.core.network.remote.AuthDataSourceImpl
import every.lol.com.feature.intro.IntroViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appDependenciesModule = module {

    single { AuthPreferences(get<DataStore<Preferences>>()) }
    single { AuthLocalDataSource(get()) }

    single<AuthDataSource> { AuthDataSourceImpl(get<HttpClient>()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    factory { LoginUseCase(get()) }
    factory { IntroViewModel(get()) }
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