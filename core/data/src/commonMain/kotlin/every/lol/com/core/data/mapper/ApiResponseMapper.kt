package every.lol.com.core.data.mapper

import every.lol.com.core.domain.DomainException
import every.lol.com.core.network.di.mapErrorCodeToDomainException
import every.lol.com.core.network.model.ApiResponse

inline fun <T, R> ApiResponse<T>.toResult(
@Suppress("UNCHECKED_CAST") transform: (T) -> R = { it as R },
): Result<R> = when (this) {
    is ApiResponse.Success -> {
        try {
            Result.success(transform(data))
        } catch (e: Exception) {
            Result.failure(DomainException.UnknownException(e))
        }
    }
    is ApiResponse.Failure.HttpError -> {
        Result.failure(mapErrorCodeToDomainException(code.toLong(), cause = Throwable(message)))
    }

    is ApiResponse.Failure.NetworkError -> {
        Result.failure(DomainException.NetworkException( cause = Throwable(message)))
    }
    is ApiResponse.Failure.UnknownApiError -> {
        Result.failure(DomainException.UnknownException( cause = Throwable(message)))
    }
}

fun <T> ApiResponse<T>.toResult(): Result<T> = toResult { it }