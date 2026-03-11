package every.lol.com.core.data.mapper

import every.lol.com.core.domain.DomainException
import every.lol.com.core.network.model.ApiResponse

inline fun <T, R> ApiResponse<T>.toResult(
@Suppress("UNCHECKED_CAST") transform: (T) -> R = { it as R },
): Result<R> = when (this) {
    is ApiResponse.Success -> {
        try {
            Result.success(transform(data))
        } catch (e: Exception) {
            Result.failure(DomainException.UnknownException("데이터 처리 중 오류가 발생했습니다."))
        }
    }
    is ApiResponse.Failure.HttpError -> {
        val exception = when (code) {
            400 -> DomainException.BadRequestException(message)
            401 -> DomainException.InvalidJwtTokenException(message)
            404 -> DomainException.NotFoundException(message)
            500 -> DomainException.ServerErrorException(message)
            502 -> DomainException.BadGatewayException(message)
            else -> DomainException.UnknownException(message)
        }
        Result.failure(exception)
    }
    is ApiResponse.Failure.NetworkError -> {
        Result.failure(DomainException.NetworkException(message ?: "네트워크 연결 상태를 확인해주세요."))
    }

    is ApiResponse.Failure.UnknownApiError -> {
        val technicalMessage = message ?: ""
        val friendlyMessage = if (technicalMessage.contains("JSON") || technicalMessage.contains("Illegal")) {
            "서비스 이용이 원활하지 않습니다. 잠시 후 다시 시도해주세요."
        } else {
            technicalMessage.ifEmpty { "알 수 없는 API 에러가 발생했습니다." }
        }

        Result.failure(DomainException.UnknownException(friendlyMessage))
    }
}

fun <T> ApiResponse<T>.toResult(): Result<T> = toResult { it }