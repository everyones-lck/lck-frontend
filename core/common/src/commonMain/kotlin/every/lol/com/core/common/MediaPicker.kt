package every.lol.com.core.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberMultiResourcePickerLauncher(
    onResult: (List<Any>) -> Unit
): () -> Unit