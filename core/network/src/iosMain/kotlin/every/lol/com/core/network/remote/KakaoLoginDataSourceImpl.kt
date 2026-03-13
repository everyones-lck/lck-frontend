package every.lol.com.core.network.remote

import every.lol.com.core.domain.repository.SocialLoginRepository
import kotlin.coroutines.suspendCoroutine

class KakaoLoginDataSourceImpl() : SocialLoginRepository {
    override suspend fun loginWithKakao(): Result<String> = suspendCoroutine {
        continuation {
            //Todo: iOS 카카오 로그인 구현하기
        }
    }
}