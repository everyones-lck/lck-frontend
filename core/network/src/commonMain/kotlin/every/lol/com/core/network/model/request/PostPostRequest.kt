package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

data class MediaFileRequst(
    val uriString: String,
    val isVideo: Boolean
)

data class PostPostRequest(
    val files: List<MediaFileRequst>?=null,
    val request: PostPostDetailRequest
)

@Serializable
data class PostPostDetailRequest(
    val postType: String,
    val postTitle: String,
    val postContent: String
)