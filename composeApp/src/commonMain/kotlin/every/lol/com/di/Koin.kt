package every.lol.com.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import every.lol.com.core.data.di.repositoryModule
import every.lol.com.core.data.repository.AboutLCKRepositoryImpl
import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.data.repository.CommunityRepositoryImpl
import every.lol.com.core.data.repository.HomeRepositoryImpl
import every.lol.com.core.data.repository.MatchesRepositoryImpl
import every.lol.com.core.data.repository.MyPageRepositoryImpl
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.datastore.AuthPreferences
import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.domain.repository.HomeRepository
import every.lol.com.core.domain.repository.MatchesRepository
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.domain.usecase.CheckAuthUseCase
import every.lol.com.core.domain.usecase.DeleteCommentUseCase
import every.lol.com.core.domain.usecase.DeletePostUseCase
import every.lol.com.core.domain.usecase.GetCommunityPopularPostsUseCase
import every.lol.com.core.domain.usecase.GetCommunityPostsUseCase
import every.lol.com.core.domain.usecase.GetHomeAlertsUseCase
import every.lol.com.core.domain.usecase.GetHomeNewsUseCase
import every.lol.com.core.domain.usecase.GetHomeRankingUseCase
import every.lol.com.core.domain.usecase.GetHomeTodayMatchUseCase
import every.lol.com.core.domain.usecase.GetMatchPogCandidateUseCase
import every.lol.com.core.domain.usecase.GetMatchPogResultUseCase
import every.lol.com.core.domain.usecase.GetMatchVoteRateUseCase
import every.lol.com.core.domain.usecase.GetMatchesCandidateUseCase
import every.lol.com.core.domain.usecase.GetMatchesUseCase
import every.lol.com.core.domain.usecase.GetMyCommentsUseCase
import every.lol.com.core.domain.usecase.GetMyPostsUseCase
import every.lol.com.core.domain.usecase.GetProfileUseCase
import every.lol.com.core.domain.usecase.GetReadPostUseCase
import every.lol.com.core.domain.usecase.GetSetPogCandidateUseCase
import every.lol.com.core.domain.usecase.GetSetPogResultUseCase
import every.lol.com.core.domain.usecase.GetSupportTeamUseCase
import every.lol.com.core.domain.usecase.LogoutUseCase
import every.lol.com.core.domain.usecase.NicknameUseCase
import every.lol.com.core.domain.usecase.PatchMyTeamUseCase
import every.lol.com.core.domain.usecase.PatchProfileUseCase
import every.lol.com.core.domain.usecase.PostCommunityCommentUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostLikeUseCase
import every.lol.com.core.domain.usecase.PostCommunityPostUseCase
import every.lol.com.core.domain.usecase.PostMatchPogVoteUseCase
import every.lol.com.core.domain.usecase.PostMatchVoteUseCase
import every.lol.com.core.domain.usecase.PostSetPogVoteUseCase
import every.lol.com.core.domain.usecase.ReportCommentUseCase
import every.lol.com.core.domain.usecase.ReportPostUseCase
import every.lol.com.core.domain.usecase.SignupUseCase
import every.lol.com.core.domain.usecase.SocialLoginUseCase
import every.lol.com.core.domain.usecase.WithdrawalUseCase
import every.lol.com.core.domain.usecase.aboutlck.GetAboutLCKMatchUseCase
import every.lol.com.core.network.datasource.AboutLCKDataSource
import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.datasource.HomeDataSource
import every.lol.com.core.network.datasource.MatchesDataSource
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.di.dataSourceModule
import every.lol.com.core.network.di.networkModule
import every.lol.com.core.notification.di.notificationModule
import every.lol.com.core.network.remote.AboutLCKDataSourceImpl
import every.lol.com.core.network.remote.AuthDataSourceImpl
import every.lol.com.core.network.remote.CommunityDataSourceImpl
import every.lol.com.core.network.remote.HomeDataSourceImpl
import every.lol.com.core.network.remote.MatchesDataSourceImpl
import every.lol.com.core.network.remote.MyPagesDataSourceImpl
import every.lol.com.feature.aboutlck.AboutLCKViewModel
import every.lol.com.feature.community.CommunityViewModel
import every.lol.com.feature.home.HomeViewModel
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

val appDependenciesModule = module {

    single { AuthPreferences(get<DataStore<Preferences>>()) }
    single { AuthLocalDataSource(get()) }

    single<AuthDataSource> { AuthDataSourceImpl(get(named("noAuth"))) }
    single<HomeDataSource> { HomeDataSourceImpl(get(named("auth"))) }
    single<MyPagesDataSource> { MyPagesDataSourceImpl(get(named("auth"))) }
    single<CommunityDataSource> { CommunityDataSourceImpl( get(), get(named("auth")), get(), get()) }
    single<AboutLCKDataSource> { AboutLCKDataSourceImpl(get(named("auth"))) }
    single<MatchesDataSource> { MatchesDataSourceImpl(get(named("auth"))) }


    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<MyPagesRepository> { MyPageRepositoryImpl(get(), get()) }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    single<CommunityRepository> { CommunityRepositoryImpl(get(), get()) }
    single<AboutLCKRepository> { AboutLCKRepositoryImpl(get(), get()) }
    single<MatchesRepository> { MatchesRepositoryImpl(get(), get()) }


    factory { SocialLoginUseCase(get(), get()) }
    factory { SignupUseCase(get()) }
    factory { NicknameUseCase(get()) }
    factory { CheckAuthUseCase(get()) }
    factory { GetSupportTeamUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { PatchProfileUseCase(get()) }
    factory { PatchMyTeamUseCase(get()) }
    factory { GetMyPostsUseCase(get()) }
    factory { GetMyCommentsUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { WithdrawalUseCase(get()) }
    factory { GetCommunityPostsUseCase(get()) }
    factory { GetCommunityPopularPostsUseCase(get()) }
    factory { GetReadPostUseCase(get())}
    factory { PostCommunityPostUseCase(get()) }
    factory { DeletePostUseCase(get()) }
    factory { ReportPostUseCase(get()) }
    factory { PostCommunityCommentUseCase(get()) }
    factory { PostCommunityPostLikeUseCase(get()) }
    factory { DeleteCommentUseCase(get()) }
    factory { ReportCommentUseCase(get()) }
    factory { GetHomeTodayMatchUseCase(get()) }
    factory { GetHomeNewsUseCase(get()) }
    factory { GetHomeAlertsUseCase(get()) }
    factory { GetHomeRankingUseCase(get()) }
    factory { GetMatchesUseCase(get()) }
    factory { GetMatchVoteRateUseCase(get()) }
    factory { PostMatchVoteUseCase(get()) }
    factory { PostSetPogVoteUseCase(get()) }
    factory { PostMatchPogVoteUseCase(get()) }
    factory { GetSetPogResultUseCase(get()) }
    factory { GetMatchPogResultUseCase(get()) }

    factory { GetMatchPogCandidateUseCase(get()) }
    factory { GetMatchesCandidateUseCase(get()) }
    factory { GetSetPogCandidateUseCase(get()) }

    factory { GetAboutLCKMatchUseCase(get())  }

    factoryOf(::IntroViewModel)
    factoryOf(::HomeViewModel)
    factoryOf(::MypageViewModel)
    factoryOf(::CommunityViewModel)
    factoryOf(::MatchesViewModel)
    factoryOf(::AboutLCKViewModel)
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
            notificationModule,
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