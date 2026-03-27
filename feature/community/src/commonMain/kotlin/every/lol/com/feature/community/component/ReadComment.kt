package every.lol.com.feature.community.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.CommentList
import every.lol.com.core.ui.component.ProfileImage
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_more
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReadComment(
    comment: CommentList,
    isReply: Boolean = false,
    onMoreClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (isReply) {
            Spacer(Modifier.width(20.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileImage(
                        modifier = Modifier.size(16.dp),
                        profile = comment.profileImageUrl
                    )
                    Text(
                        text = comment.nickname,
                        style = EveryLoLTheme.typography.caption01,
                        color = EveryLoLTheme.color.grayScale800
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onMoreClick),
                    painter = painterResource(Res.drawable.ic_more),
                    contentDescription = null,
                    tint = EveryLoLTheme.color.community600
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = comment.content,
                    style = EveryLoLTheme.typography.pretendardBody02,
                    color = EveryLoLTheme.color.community600
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = comment.createdAt,
                    style = EveryLoLTheme.typography.caption02,
                    color = EveryLoLTheme.color.grayScale800
                )
            }
        }
    }
}