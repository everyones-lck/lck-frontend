package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_no_content
import org.jetbrains.compose.resources.painterResource


@Composable
fun PopularPost(
    modifier: Modifier = Modifier,
    popularPosts: List<Any>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(16.dp,8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (popularPosts.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_no_content),
                    contentDescription = null,
                    tint = EveryLoLTheme.color.community600
                )
                Text(
                    text = "인기글이 아직 없습니다",
                    style = EveryLoLTheme.typography.subtitle03,
                    color = EveryLoLTheme.color.community600
                )
            }
        } else {
            popularPosts.forEach { post ->
                PopularPostItem(
                    category = "잡담",
                    title = "16글자가 넘어가면 말줄임표가 생기는 테스트 제목입니다",
                    createDate = "02.24",
                    postId = 0
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
    postId: Int
){
    Row(
        modifier = modifier.fillMaxWidth().padding(16.dp, 8.dp),
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
            style = EveryLoLTheme.typography.body02,
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