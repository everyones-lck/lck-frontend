package every.lol.com.core.domain

sealed class DomainException(
    message: String? = null,
    cause: Throwable? = null
) : Throwable(message, cause) {

    // 인증 관련 (1000L, 2001L, 1001L)
    class InvalidJwtTokenException(cause: Throwable? = null) : DomainException("유효하지 않은 토큰입니다.", cause)
    class UnsupportedSocialProviderException(cause: Throwable? = null) : DomainException("지원하지 않는 소셜 로그인 제공자입니다.", cause)

    // 유저 관련 (2000L, 3000L)
    class AlreadyRegisteredUserException(cause: Throwable? = null) : DomainException("이미 가입된 유저입니다.", cause)
    class UserNotFoundException(cause: Throwable? = null) : DomainException("존재하지 않는 유저입니다.", cause)

    // 권한 및 공통 에러 (5002L, 9000L, 9001L)
    class NoPermissionException(cause: Throwable? = null) : DomainException("권한이 없습니다.", cause)
    class ServerErrorException(cause: Throwable? = null) : DomainException("서버 내부 에러가 발생했습니다.", cause)
    class InvalidInputException(cause: Throwable? = null) : DomainException("잘못된 입력값입니다.", cause)

    // 시스템 및 기타 에러 (Network, Unknown)
    class NetworkException(cause: Throwable? = null) : DomainException("네트워크 연결 상태를 확인해주세요.", cause)
    class UnknownException(cause: Throwable? = null) : DomainException("알 수 없는 에러가 발생했습니다.", cause)

}