package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostIdResponse(
    val postId: Int
)