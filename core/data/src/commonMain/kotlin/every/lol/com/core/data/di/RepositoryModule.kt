package every.lol.com.core.data.di

import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.data.repository.SocialLoginRepositoryImpl
import every.lol.com.core.domain.repository.AuthRepository
import every.lol.com.core.domain.repository.SocialLoginRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<SocialLoginRepository> { SocialLoginRepositoryImpl(get()) }
}