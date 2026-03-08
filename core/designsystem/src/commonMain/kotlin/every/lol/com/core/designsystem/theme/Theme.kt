package every.lol.com.core.designsystem.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun EveryLoLTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    BoxWithConstraints {
        val currentDensity = LocalDensity.current

        val standardWidth = 360f

        val fixedDensityValue = (maxWidth.value / standardWidth) * currentDensity.density

        val fixedDensity = Density(
            density = fixedDensityValue,
            fontScale = 1f
        )

        val colors: EveryLoLColor = EveryLoLDarkColor
        val typography = everyLoLTypography

        CompositionLocalProvider(
            LocalColor provides colors,
            LocalDensity provides fixedDensity,
            LocalTypography provides typography
        ) {
            content()
        }
    }
}

object EveryLoLTheme {
    val color: EveryLoLColor
        @Composable
        get() = LocalColor.current

    val typography: EveryLoLTypography
        @Composable
        get() = LocalTypography.current
}