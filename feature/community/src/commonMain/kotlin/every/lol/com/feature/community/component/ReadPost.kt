package every.lol.com.feature.community.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.PostBlock
import every.lol.com.core.model.PostDetail
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_more
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReadPost(
    postDetail: PostDetail,
    contentBlocks: List<PostBlock>,
    onMoreClick: () -> Unit,
    onImageClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    onLikeClick: () -> Unit = {},
    isCommented: Boolean = false,
    isLiked: Boolean = false,
    likeCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommunityPostHeader(
                profile = postDetail.writerProfileUrl,
                nickname = postDetail.writerNickname,
                date = postDetail.postCreatedAt
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onMoreClick),
                painter = painterResource(Res.drawable.ic_more),
                contentDescription = null,
                tint = EveryLoLTheme.color.community600
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = postDetail.postTitle,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.white200
        )

        contentBlocks.forEach { block ->
            when(block){
                is PostBlock.Text -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = block.text,
                        style = EveryLoLTheme.typography.pretendardBody02,
                        color = EveryLoLTheme.color.community600
                    )
                }
                is PostBlock.Image -> {
                    AsyncImage(
                        model = block.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(11.dp))
                            .clickable { onImageClick(block.imageUrl) },
                        contentScale = ContentScale.FillHeight
                    )
                }
                is PostBlock.Video -> {
                    CommunityVideo(
                        videoUrl = block.videoUrl,
                        onVideoClick = onVideoClick
                    )
                }
            }
        }

        CommunityPostActions(
            isCommented = isCommented,
            isLiked = isLiked,
            commentCount = postDetail.commentCount,
            likeCount = likeCount,
            viewCount = 0,
            onLikeClick = onLikeClick
        )
    }
}