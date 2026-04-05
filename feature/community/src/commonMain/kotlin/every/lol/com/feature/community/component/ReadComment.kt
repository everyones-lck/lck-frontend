package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.CommentItem
import every.lol.com.core.ui.component.ProfileImage
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_more
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReadComment(
    comment: CommentItem,
    isReply: Boolean = false,
    onClick:() -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    var selectedComment by remember { mutableStateOf<CommentItem?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clickable(enabled = !comment.isDeleted, onClick = onClick)
                .padding(bottom = 8.dp)
                .fillMaxWidth()) {
            if (isReply) {
                Spacer(Modifier.width(20.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
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
                            profile = if (comment.isDeleted) "" else comment.profileImageUrl
                        )
                        Text(
                            text = if (comment.isDeleted) "(알 수 없음)" else comment.nickname,
                            style = EveryLoLTheme.typography.caption01,
                            color = EveryLoLTheme.color.grayScale800
                        )
                    }
                    if (!comment.isDeleted) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { selectedComment = comment },
                            painter = painterResource(Res.drawable.ic_more),
                            contentDescription = null,
                            tint = EveryLoLTheme.color.community600
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = comment.content,
                        style = EveryLoLTheme.typography.pretendardBody02,
                        color = if (comment.isDeleted) EveryLoLTheme.color.grayScale800 else EveryLoLTheme.color.community600
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

        if (selectedComment != null) {
            CommentMenu(
                isMine = selectedComment?.isWriter ?: false,
                onDismiss = { selectedComment = null },
                onDelete = onDelete,
                onReport = onReport
            )
        }
    }
}

@Composable
fun CommentMenu(
    isMine: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopEnd).size(0.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .background(EveryLoLTheme.color.black900,RoundedCornerShape(12.dp)),
                offset = DpOffset(x = (0).dp, y = 0.dp)
            ) {
                if (isMine) {
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "삭제하기",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        onClick = { onDelete(); onDismiss() }
                    )
                } else {
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "신고하기",
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.community600,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        onClick = { onReport(); onDismiss() }
                    )
                }
            }
        }
    }
}
