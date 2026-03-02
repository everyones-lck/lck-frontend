package every.lol.com.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect

@Composable
fun EveryLoLTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: EveryLoLColor = EveryLoLDarkColor
    val typography = everyLoLTypography

    //val view = LocalView.current

    /*
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            // 앱이 시스템 패딩을 직접 제어
            WindowCompat.setDecorFitsSystemWindows(window, false)

            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme

            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    */

    CompositionLocalProvider(
        LocalColor provides colors,
        LocalTypography provides typography,
        content = content
    )
}

object EveryLoLTheme {
    val color: EveryLoLColor
        @Composable
        get() = LocalColor.current

    val typography: EveryLoLTypography
        @Composable
        get() = LocalTypography.current
}