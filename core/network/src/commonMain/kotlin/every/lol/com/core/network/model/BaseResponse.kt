package every.lol.com.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val message: String,
    val data: T,
    val success: Boolean
)