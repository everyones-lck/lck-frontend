package every.lol.com.core.network.datasource

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class SocialLoginDataSource {

    actual suspend fun loginWithKakao(): Result<String> = suspendCoroutine { continuation ->
        val bridge = IOSKakaoLoginBridgeProvider.bridge

        if (bridge == null) {
            continuation.resume(
                Result.failure(Exception("IOSKakaoLoginBridge is not initialized"))
            )
            return@suspendCoroutine
        }

        bridge.loginWithKakao(
            object : IOSKakaoLoginCallback {
                override fun onSuccess(userId: String) {
                    continuation.resume(Result.success(userId))
                }

                override fun onFailure(message: String) {
                    continuation.resume(Result.failure(Exception(message)))
                }
            }
        )
    }
}