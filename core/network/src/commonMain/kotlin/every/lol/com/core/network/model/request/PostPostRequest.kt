package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

data class MediaFileRequst(
    val uriString: String,
    val fileSize: Long,
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
    val blocks: List<BlocksRequest>
)

@Serializable
data class BlocksRequest(
    val sequence: Int,
    val type: String,
    val content: String?=null,
    val fileIndex: Int?=null,
    val thumbnailFileIndex: Int?=null
)