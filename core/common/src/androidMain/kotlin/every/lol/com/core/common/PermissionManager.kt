package every.lol.com.core.common

import androidx.compose.runtime.Composable
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.values

@Composable
actual fun rememberPermissionManager(
    onPermissionResult: (PermissionType, Boolean) -> Unit
): PermissionHandler {
    val context = LocalContext.current

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        onPermissionResult(PermissionType.LOCATION, granted)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(PermissionType.GALLERY, isGranted)
    }

    return remember {
        object : PermissionHandler {
            override fun askPermission(type: PermissionType) {
                when (type) {
                    PermissionType.LOCATION -> {
                        locationLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    PermissionType.GALLERY -> {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        galleryLauncher.launch(permission)
                    }
                }
            }

            // [추가] 인터페이스의 미구현 멤버 구현
            override fun isPermissionGranted(type: PermissionType): Boolean {
                return when (type) {
                    PermissionType.LOCATION -> {
                        context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                                context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                    PermissionType.GALLERY -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            context.hasPermission(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            context.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            }
        }
    }
}

// 헬퍼 확장 함수
private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}