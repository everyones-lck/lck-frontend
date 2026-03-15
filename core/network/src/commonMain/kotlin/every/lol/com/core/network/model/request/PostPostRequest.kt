package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable


@Serializable
data class PostPostRequest(
    val files: List<String>,
    val request: PostPostDetailRequest
)

@Serializable
data class PostPostDetailRequest(
    val postType: String,
    val postTitle: String,
    val postContent: String
)