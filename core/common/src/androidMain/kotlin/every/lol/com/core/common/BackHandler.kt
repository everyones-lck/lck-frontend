package every.lol.com.core.common

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun EveryLolBackHandler(enabled: Boolean,onBack: () -> Unit) {
    BackHandler(enabled, onBack)
}