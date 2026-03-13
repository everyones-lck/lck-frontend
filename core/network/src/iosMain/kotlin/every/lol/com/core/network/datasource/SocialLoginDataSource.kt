package every.lol.com.core.network.datasource


actual class SocialLoginDataSource {
    actual suspend fun loginWithKakao(): Result<String> {
        //Todo: iOS 카카오 로그인 구현하기
        return Result.failure(Exception("iOS Kakao Login not implemented"))
    }
}