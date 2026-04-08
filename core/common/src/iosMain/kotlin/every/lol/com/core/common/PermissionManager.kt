package every.lol.com.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
actual fun rememberPermissionManager(
    onPermissionResult: (PermissionType, Boolean) -> Unit
): PermissionHandler {
    val currentCallback by rememberUpdatedState(onPermissionResult)

    return remember {
        object : PermissionHandler {
            override fun askPermission(type: PermissionType) {
                when (type) {
                    PermissionType.GALLERY -> currentCallback(type, true)
                    // TODO: 기반 실제 권한 요청 구현 필요
                    PermissionType.LOCATION -> currentCallback(type, false)
                    PermissionType.AlARM -> currentCallback(type, true) //Todo: 알람 권한 요청 구현 필요
                }
            }

            override fun isPermissionGranted(type: PermissionType): Boolean {
                return when (type) {
                    PermissionType.GALLERY -> true
                    PermissionType.LOCATION -> false
                    PermissionType.AlARM -> true //Todo: 알람 권한 요청 구현 필요
                }
            }
        }
    }
}