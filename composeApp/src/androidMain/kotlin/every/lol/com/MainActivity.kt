package every.lol.com

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.di.androidModule
import every.lol.com.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bgColor = 0xFF131313.toInt()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(bgColor),
            navigationBarStyle = SystemBarStyle.dark(bgColor)
        )

        super.onCreate(savedInstanceState)
        if (GlobalContext.getOrNull() == null) {
            initKoin {
                androidContext(applicationContext)
                androidLogger(Level.DEBUG)
                modules(androidModule)
            }
        }
        window.setBackgroundDrawable(ColorDrawable(bgColor))

        setContent {
            EveryLoLTheme {
                App()
            }
        }
    }
}