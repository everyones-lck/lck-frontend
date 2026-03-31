package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostLikeResponse(
    val isLiked: Boolean,
    val likeCount: Int
)