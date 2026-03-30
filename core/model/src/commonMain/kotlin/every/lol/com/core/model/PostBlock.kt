package every.lol.com.core.model

import kotlinx.serialization.Serializable

@Serializable
sealed class PostBlock{
    @Serializable
    data class Text(val text: String) : PostBlock()
    @Serializable
    data class Image(val imageUrl: String) : PostBlock()
    @Serializable
    data class Video(val videoUrl: String, val thumbnailUrl: String) : PostBlock()
}


fun mapToUiState(serverPost: PostDetail): List<PostBlock> {
    val uiBlocks = mutableListOf<PostBlock>()

    uiBlocks.add(PostBlock.Text(serverPost.content))

    serverPost.fileList.forEach { file ->
        if (file.isImage) {
            uiBlocks.add(PostBlock.Image(file.fileUrl))
        } else {
            uiBlocks.add(PostBlock.Video(file.fileUrl, thumbnailUrl = ""))
        }
    }
    return uiBlocks
}