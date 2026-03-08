package every.lol.com.core.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberPermissionManager(
    onPermissionResult: (PermissionType, Boolean) -> Unit
): PermissionHandler

enum class PermissionType { LOCATION, GALLERY }

interface PermissionHandler {
    fun askPermission(type: PermissionType)
    fun isPermissionGranted(type: PermissionType): Boolean
}