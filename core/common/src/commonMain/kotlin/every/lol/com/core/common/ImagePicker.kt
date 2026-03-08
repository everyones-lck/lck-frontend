package every.lol.com.core.common

import androidx.compose.runtime.Composable


@Composable
expect fun rememberImagePickerLauncher(
    onResult: (Any?) -> Unit
): () -> Unit