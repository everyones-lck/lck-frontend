package every.lol.com.core.network.util

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.BaseResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

internal suspend inline fun <reified T> HttpResponse.asApiResponse(): ApiResponse<T> {
    return try { // 성공 응답 (200~299)
        val baseResponse: BaseResponse<T> = this.body()

        if (baseResponse.success) {
            ApiResponse.Success(baseResponse.data)
        } else {
            ApiResponse.Failure.HttpError(
                code = status.value,
                message = baseResponse.message,
                body = baseResponse.data.toString()
            )
        }
    } catch (e: ResponseException) { // HTTP 에러
        ApiResponse.Failure.HttpError(
            code = e.response.status.value,
            message = e.message ?: "HTTP Error",
            body = e.response.bodyAsText()
        )
    } catch (e: IOException) { // 네트워크 연결 에러
        ApiResponse.Failure.NetworkError(e)
    } catch (e: SerializationException) { // JSON 파싱 에러
        ApiResponse.Failure.UnknownApiError(e)
    } catch (e: Exception) { // 기타 에러
        ApiResponse.Failure.UnknownApiError(e)
    }
}

internal fun <T> Throwable.toApiResponse(): ApiResponse<T> {
    return when (this) {
        is IOException -> ApiResponse.Failure.NetworkError(this)
        is SerializationException -> ApiResponse.Failure.UnknownApiError(this)
        else -> ApiResponse.Failure.UnknownApiError(this)
    }
}