package every.lol.com.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPermissionManager(
    onPermissionResult: (PermissionType, Boolean) -> Unit
): PermissionHandler {
    return remember {
        object : PermissionHandler {
            override fun askPermission(type: PermissionType) {

            }

            override fun isPermissionGranted(type: PermissionType): Boolean {

                return false
            }
        }
    }
}