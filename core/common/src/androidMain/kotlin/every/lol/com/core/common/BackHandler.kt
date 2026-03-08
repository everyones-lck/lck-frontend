package every.lol.com.core.common

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun EveryLolBackHandler(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
}