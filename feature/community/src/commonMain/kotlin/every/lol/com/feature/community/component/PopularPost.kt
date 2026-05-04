package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.PopularPostListDetail
import every.lol.com.core.ui.component.EmptyContent


@Composable
fun PopularPost(
    modifier: Modifier = Modifier,
    popularPosts: List<PopularPostListDetail>,
    onPostClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(16.dp,8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (popularPosts.isEmpty()) {
            EmptyContent("no_content", "인기글이 아직 없습니다.", Modifier.height(200.dp))
        } else {
            popularPosts.forEach { post ->
                PopularPostItem(
                    category = post.postTypeName,
                    title = post.postTitle,
                    createDate = post.postCreatedAt,
                    onClick = { onPostClick(post.postId) }
                )
            }
        }
    }
}

@Composable
private fun PopularPostItem(
    modifier: Modifier = Modifier,
    category: String,
    title: String,
    createDate: String,
    onClick: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        CategoryBox(category = category)
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.community600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = createDate,
            style = EveryLoLTheme.typography.pretendardBody02,
            color = EveryLoLTheme.color.gray800
        )
    }
}

@Composable
fun CategoryBox(
    modifier: Modifier = Modifier,
    category: String
){
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(EveryLoLTheme.color.grayScale900)
            .padding(8.dp, 4.dp),
        text = category,
        style = EveryLoLTheme.typography.body03,
        color = EveryLoLTheme.color.community600
    )
}