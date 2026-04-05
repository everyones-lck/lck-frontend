package every.lol.com.core.common

import androidx.compose.runtime.Composable


@Composable
expect fun EveryLolBackHandler(enabled: Boolean,onBack: () -> Unit)