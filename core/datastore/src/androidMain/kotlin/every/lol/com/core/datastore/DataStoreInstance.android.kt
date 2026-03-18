package every.lol.com.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = {
            context.filesDir.resolve(AUTH_DATASTORE_FILE_NAME).absolutePath
        }
    )