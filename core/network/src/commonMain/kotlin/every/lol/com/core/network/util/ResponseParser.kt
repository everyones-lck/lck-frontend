package every.lol.com.core.network.util

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.BaseResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

internal suspend inline fun <reified T> Result<HttpResponse>.asApiResponse(): ApiResponse<T> {
    val response = this.getOrNull()
    val exception = this.exceptionOrNull()

    if (exception != null) {
        return ApiResponse.Failure.NetworkError(
            message = exception.message ?: "Network Connection Failed",
            throwable = exception.stackTraceToString()
        )
    }

    return try {
        if (response == null) throw Exception("Response is null")

        val baseResponse: BaseResponse<T> = response.body()
        if (baseResponse.success) {
            ApiResponse.Success(baseResponse.data ?: throw Exception("Response data is null"))
        } else {
            ApiResponse.Failure.HttpError(
                code = response.status.value,
                message = baseResponse.message
            )
        }
    } catch (e: Exception) {
        ApiResponse.Failure.UnknownApiError(
            message = e.message ?: "Unknown Error",
            throwable = e.stackTraceToString()
        )
    }
}