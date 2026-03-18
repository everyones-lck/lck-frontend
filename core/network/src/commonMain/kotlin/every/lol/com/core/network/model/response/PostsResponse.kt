package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class PostsResponse(
    val posts: List<PostsDetailResponse>,
    val isLast: Boolean
)

@Serializable
data class PostsDetailResponse(
    val id: Int,
    val title: String,
    val postType: String
)