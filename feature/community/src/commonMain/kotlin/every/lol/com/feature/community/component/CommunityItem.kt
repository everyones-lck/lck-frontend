package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.PostListDetail
import every.lol.com.feature.community.model.CommunityUiState

@Composable
fun CommunityItem(
    type: CommunityUiState.CommunityTab,
    post: PostListDetail,
    onPostClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .clickable(onClick = onPostClick)
            .padding(20.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        CommunityPostHeader(
            profile = post.userProfilePicture,
            nickname = post.userNickname,
            date = post.postCreatedAt
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
            Text(
                text = post.postTitle,
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = post.postContent,
                style = EveryLoLTheme.typography.body02,
                color = EveryLoLTheme.color.community600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!post.thumbnailFileUrl.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    postThumbnail(
                        image = post.thumbnailFileUrl,
                        totalCounts = 10
                    )
                }
            }

        }
        CommunityPostActions(
            type = type,
            commentCount = post.commentCounts,
            likeCount = 0,
            viewCount = 0
        )
    }
}

@Composable
private fun postThumbnail(
    image : Any?,
    modifier: Modifier = Modifier,
    totalCounts: Int
){
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(EveryLoLTheme.color.grayScale500.copy(alpha = 0.16f))
    ){
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        /*Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(3.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(EveryLoLTheme.color.grayScale600)
                .padding(1.dp),
            text = "1/${totalCounts}",
            style = EveryLoLTheme.typography.caption02,
            color = EveryLoLTheme.color.grayScale800,
        )*/
    }
}