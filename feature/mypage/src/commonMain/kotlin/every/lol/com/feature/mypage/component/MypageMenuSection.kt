package every.lol.com.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.mypage.model.MypageUiState

@Composable
fun MypageMenuSection(
    modifier: Modifier = Modifier,
    menuItems: List<MypageUiState.MypageMenu>,
    onMenuClick: (MypageUiState.MypageMenuType) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(14.dp,8.dp)
    ){
        menuItems.forEachIndexed { index, menu ->
            MypageMenuRow(
                title = menu.title,
                onClick = { onMenuClick(menu.id) }
            )
            if (index < menuItems.size - 1 && menu.showDivider) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = EveryLoLTheme.color.grayScale900
                )
            }
        }
    }
}

@Composable
private fun MypageMenuRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = EveryLoLTheme.typography.pretendardBody01,
            color = EveryLoLTheme.color.community600
        )
    }
}