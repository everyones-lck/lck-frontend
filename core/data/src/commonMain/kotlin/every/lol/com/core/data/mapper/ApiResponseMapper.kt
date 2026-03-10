package every.lol.com.core.data.mapper

import every.lol.com.core.domain.DomainException
import every.lol.com.core.network.model.ApiResponse

inline fun <T, R> ApiResponse<T>.toResult(
@Suppress("UNCHECKED_CAST") transform: (T) -> R = { it as R },
): Result<R> = when (this) {
    is ApiResponse.Success -> {
        runCatching {transform(data) }
    }
    is ApiResponse.Failure.HttpError -> {
        Result.failure(mapErrorCodeToDomainException(code.toLong()))
    }

    is ApiResponse.Failure.NetworkError -> {
        Result.failure(DomainException.NetworkException(cause = throwable))
    }
    is ApiResponse.Failure.UnknownApiError -> {
        Result.failure(DomainException.UnknownException(cause = throwable))
    }
}

fun <T> ApiResponse<T>.toResult(): Result<T> = toResult { it }

fun mapErrorCodeToDomainException(errorCode: Long, cause: Throwable? = null): DomainException {
    return when (errorCode) {
        // 인증 관련
        1000L, 2001L -> DomainException.InvalidJwtTokenException(cause = cause)
        1001L -> DomainException.UnsupportedSocialProviderException(cause = cause)

        // 유저 관련
        2000L -> DomainException.AlreadyRegisteredUserException(cause = cause)
        3000L -> DomainException.UserNotFoundException(cause = cause)

        // 권한 및 공통 에러
        5002L -> DomainException.NoPermissionException(cause = cause)
        9000L -> DomainException.ServerErrorException(cause = cause)
        9001L -> DomainException.InvalidInputException(cause = cause)

        // 기본 에러
        else -> DomainException.UnknownException(cause = cause)
    }
}