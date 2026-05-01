package every.lol.com.core.model

import kotlinx.serialization.Serializable

@Serializable
sealed class PostBlock{
    @Serializable
    data class Text(val text: String) : PostBlock()
    @Serializable
    data class Image(val imageUrl: String) : PostBlock()
    @Serializable
    data class Video(val videoUrl: String, val thumbnailUrl: String?=null) : PostBlock()
}

fun mapToUiState(serverPost: PostDetail): List<PostBlock> {
    return serverPost.blocks
        .sortedBy { it.sequence }
        .mapNotNull { block ->
            when (block.type) {
                "TEXT" -> {
                    block.content?.let { PostBlock.Text(text = it) }
                }

                "IMAGE" -> {
                    block.fileUrl?.let { PostBlock.Image(imageUrl = it) }
                }

                "VIDEO" -> {
                    block.fileUrl?.let {
                        PostBlock.Video(
                            videoUrl = it,
                            thumbnailUrl = null
                        )
                    }
                }

                else -> null
            }
        }
}