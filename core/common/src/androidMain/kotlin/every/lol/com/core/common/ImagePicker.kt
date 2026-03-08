package every.lol.com.core.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@androidx.compose.runtime.Composable
actual fun rememberImagePickerLauncher(onResult: (Any?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onResult(uri)
    }
    return { launcher.launch("image/*") }
}