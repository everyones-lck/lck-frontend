package every.lol.com.core.network.model.request

import kotlinx.serialization.Serializable

data class EditPostRequest(
    val request: EditPostDetailRequest,
    val newFiles: List<MediaFileRequst>?=null
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