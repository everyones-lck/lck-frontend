package every.lol.com.core.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import every.lol.com.core.datastore.createDataStore
import every.lol.com.core.network.datasource.SocialLoginDataSource
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformDataSourceModule = module {
    single { SocialLoginDataSource(get()) }
    single<DataStore<Preferences>> { createDataStore(get<Context>()) }
    single<HttpClientEngineFactory<*>> { OkHttp }
}