package every.lol.com.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import every.lol.com.core.data.di.repositoryModule
import every.lol.com.core.data.repository.AboutLCKRepositoryImpl
import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.data.repository.CommunityRepositoryImpl
import every.lol.com.core.data.repository.MyPageRepositoryImpl
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.datastore.AuthPreferences
import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.domain.usecase.CheckAuthUseCase
import every.lol.com.core.domain.usecase.GetMyCommentsUseCase
import every.lol.com.core.domain.usecase.GetMyPostsUseCase
import every.lol.com.core.domain.usecase.GetProfileUseCase
import every.lol.com.core.domain.usecase.LogoutUseCase
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.domain.usecase.PatchMyTeamUseCase
import every.lol.com.core.domain.usecase.PatchProfileUseCase
import every.lol.com.core.domain.usecase.SignupUseCase
import every.lol.com.core.domain.usecase.SocialLoginUseCase
import every.lol.com.core.domain.usecase.WithdrawalUseCase
import every.lol.com.core.network.datasource.AboutLCKDataSource
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.di.dataSourceModule
import every.lol.com.core.network.di.networkModule
import every.lol.com.core.network.remote.AboutLCKDataSourceImpl
import every.lol.com.core.network.remote.AuthDataSourceImpl
import every.lol.com.core.network.remote.CommunityDataSourceImpl
import every.lol.com.core.network.remote.MyPagesDataSourceImpl
import every.lol.com.feature.intro.IntroViewModel
import every.lol.com.feature.matches.MatchesViewModel
import every.lol.com.feature.mypage.MypageViewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

val appDependenciesModule = module {

    single { AuthPreferences(get<DataStore<Preferences>>()) }
    single { AuthLocalDataSource(get()) }

    single<AuthDataSource> { AuthDataSourceImpl(get(named("noAuth"))) }
    single<MyPagesDataSource> { MyPagesDataSourceImpl(get(named("auth"))) }
    single<CommunityDataSource> { CommunityDataSourceImpl(get(named("auth"))) }
    single<AboutLCKDataSource> { AboutLCKDataSourceImpl(get(named("auth"))) }


    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<MyPagesRepository> { MyPageRepositoryImpl(get(), get()) }
    single<CommunityRepository> { CommunityRepositoryImpl(get(), get()) }
    single<AboutLCKRepository> { AboutLCKRepositoryImpl(get(), get()) }


    factory { SocialLoginUseCase(get(), get()) }
    factory { SignupUseCase(get()) }
    factory { NicknameUseCase(get()) }
    factory { CheckAuthUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { PatchProfileUseCase(get()) }
    factory { PatchMyTeamUseCase(get()) }
    factory { GetMyPostsUseCase(get()) }
    factory { GetMyCommentsUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { WithdrawalUseCase(get()) }

    factoryOf(::IntroViewModel)
    factoryOf(::MypageViewModel)
    factoryOf(::MatchesViewModel)
}

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    platformModules: List<Module> = emptyList()
) {
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

object KoinBridge {
    fun start() {
        initKoin()
  }
}