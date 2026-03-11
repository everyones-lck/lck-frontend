package every.lol.com.core.data.di

import every.lol.com.core.data.repository.AuthRepositoryImpl
import every.lol.com.core.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

}