package every.lol.com.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

val androidModule = module {
    single<DataStore<Preferences>> { get<Context>().authDataStore }
}