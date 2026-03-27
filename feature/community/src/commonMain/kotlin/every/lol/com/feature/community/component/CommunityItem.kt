package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
                text = "본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라본문을 내놔라",  //Todo: 본문 반영
                style = EveryLoLTheme.typography.body02,
                color = EveryLoLTheme.color.community600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        CommunityPostActions(
            type = type,
            commentCount = post.commentCounts,
            likeCount = 10,
            viewCount = 20
        )
    }
}