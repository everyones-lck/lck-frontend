package every.lol.com.core.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPlatformContext(): Any = LocalContext.current

object AppContext {
    private var applicationContext: Context? = null

    fun set(context: Context) {
        applicationContext = context.applicationContext
    }

    fun get(): Context {
        return applicationContext ?: throw IllegalStateException("AppContext is not initialized. Call AppContext.set(context) in your Application class.")
    }
}