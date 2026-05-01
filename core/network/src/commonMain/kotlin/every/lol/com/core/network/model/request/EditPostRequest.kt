package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

data class EditPostRequest(
    val newFiles: List<MediaFileRequst>?=null,
    val request: EditPostDetailRequest
)

@Serializable
data class EditPostDetailRequest(
    val postType: String,
    val postTitle: String,
    val blocks: List<EditBlocksRequest>
)

@Serializable
data class EditBlocksRequest(
    val sequence: Int,
    val type: String,
    val content: String?=null,
    val fileIndex: Int?=null,
    val existingFileUrl: String?=null
)