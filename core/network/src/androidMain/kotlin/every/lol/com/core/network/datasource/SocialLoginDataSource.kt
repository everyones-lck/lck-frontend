package every.lol.com.core.network.datasource

import android.content.Context
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class SocialLoginDataSource(private val context: Context) {

    actual suspend fun loginWithKakao(): Result<String> = suspendCoroutine { continuation ->
        val getUserIdCallback: (Throwable?) -> Unit = { error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else {
                UserApiClient.instance.me { user, meError ->
                    if (meError != null) {
                        continuation.resume(Result.failure(meError))
                    } else if (user != null) {
                        continuation.resume(Result.success(user.id.toString()))
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    if (error is com.kakao.sdk.common.model.ClientError &&
                        error.reason == com.kakao.sdk.common.model.ClientErrorCause.Cancelled) {
                        continuation.resume(Result.failure(error))
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context) { _, accountError ->
                        getUserIdCallback(accountError)
                    }
                } else {
                    getUserIdCallback(null)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { _, error ->
                getUserIdCallback(error)
            }
        }
    }
}