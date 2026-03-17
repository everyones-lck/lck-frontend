package every.lol.com.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.mypage.model.MypageUiState
import everylol.feature.mypage.generated.resources.Res
import everylol.feature.mypage.generated.resources.ic_comment
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommunityItem(
    type: MypageUiState.CommunityTab,
    title: String?= null,
    content: String?= null,
    date: String = "YY.MM.DD",
    postType: String = "게시판"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            TextSection(date) //Todo: 서버 수정 후 날짜 적용
            TextSection(postType)
        }
        Text(
            text = title ?: if (type == MypageUiState.CommunityTab.COMMENT) { "댓글을 남긴 게시글 제목 (데이터 추가 필요)" } else { "제목 없음" },
            style = EveryLoLTheme.typography.subtitle02,
            color = EveryLoLTheme.color.white200,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ){
            if(type == MypageUiState.CommunityTab.COMMENT){
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.ic_comment),
                    contentDescription = null,
                    tint = EveryLoLTheme.color.gray800
                )
            }

            Text(
                text = content?: if(type == MypageUiState.CommunityTab.COMMENT) { "댓글 내용 (데이터 추가 필요)" } else { "내용 없음 "},
                style = EveryLoLTheme.typography.pretendardBody02,
                color = EveryLoLTheme.color.community600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = EveryLoLTheme.color.grayScale900
        )
    }
}


@Composable
private fun TextSection(
    text : String
){
    Text(
        modifier = Modifier
            .background(EveryLoLTheme.color.grayScale900)
            .clip(RoundedCornerShape(4.dp))
            .padding(4.dp, 3.dp),
        text = text,
        style = EveryLoLTheme.typography.label03,
        color = EveryLoLTheme.color.white200
    )
}