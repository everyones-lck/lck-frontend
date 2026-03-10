package every.lol.com.core.network.model

data class BaseResponse<T>(
    val message: String,
    val data: T,
    val success: Boolean
)