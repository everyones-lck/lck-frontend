package every.lol.com.core.data.di

import every.lol.com.core.data.repository.AboutLCKRepositoryImpl
import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.data.repository.CommunityRepositoryImpl
import every.lol.com.core.data.repository.MyPageRepositoryImpl
import every.lol.com.core.data.repository.SocialLoginRepositoryImpl
import every.lol.com.core.domain.repository.AboutLCKRepository
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.CommunityRepository
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.domain.repository.SocialLoginRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<MyPagesRepository> { MyPageRepositoryImpl(get(), get()) }
    single<CommunityRepository> { CommunityRepositoryImpl(get(), get()) }
    single<AboutLCKRepository> { AboutLCKRepositoryImpl(get(), get()) }
    single<SocialLoginRepository> { SocialLoginRepositoryImpl(get()) }
}