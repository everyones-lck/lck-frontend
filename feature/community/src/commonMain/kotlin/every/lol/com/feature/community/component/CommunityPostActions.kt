package every.lol.com.feature.community.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.community.model.CommunityUiState
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_community_comment
import everylol.feature.community.generated.resources.ic_community_like
import everylol.feature.community.generated.resources.ic_community_views
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommunityPostActions(
    type: CommunityUiState.CommunityTab?=null,
    commentCount: Int,
    likeCount: Int,
    viewCount: Int
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(type == CommunityUiState.CommunityTab.ALL){
            CategoryBox(category = type.toString()) //Todo: 카테고리 반영
        }
        CountBox(type = 0, count = likeCount, onCountClick = {}) //Todo: 좋아요 개수
        CountBox(type = 1, count = viewCount, onCountClick = {}) //Todo: 조회수 개수
        CountBox(type = 2, count = commentCount, onCountClick = {})
    }
}


@Composable
fun CountBox(
    type: Int, //0: 좋아요, 1: 조회수, 2: 댓글
    count: Int,
    onCountClick: (type: Int) -> Unit
){
    val icon = when (type) {
        0 -> painterResource(Res.drawable.ic_community_like)
        1 -> painterResource(Res.drawable.ic_community_views)
        2 -> painterResource(Res.drawable.ic_community_comment)
        else -> painterResource(Res.drawable.ic_community_like)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(7.dp))
            .border(
                width = 1.dp,
                color = EveryLoLTheme.color.grayScale900,
                shape = RoundedCornerShape(7.dp)
            )
            .padding(8.dp, 4.dp)
            .clickable { onCountClick(type) } ,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            painter = icon,
            contentDescription = null,
            tint = EveryLoLTheme.color.gray800,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = count.toString(),
            style = EveryLoLTheme.typography.subtitle04,
            color = EveryLoLTheme.color.gray800
        )
    }
}