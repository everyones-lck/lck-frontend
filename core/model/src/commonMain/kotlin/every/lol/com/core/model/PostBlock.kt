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
    // 서버에서 준 blocks 리스트를 순회하며 PostBlock으로 변환
    return serverPost.blocks
        .sortedBy { it.sequence } // 혹시 모르니 순서대로 정렬
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
                            thumbnailUrl = null // 서버에서 안 주므로 null 처리
                        )
                    }
                }

                else -> null
            }
        }
}