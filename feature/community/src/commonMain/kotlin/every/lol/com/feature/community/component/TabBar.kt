package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme


@Composable
fun <T> TabBar(
    modifier: Modifier = Modifier,
    tabItems: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    getDisplayName: (T) -> String // 각 탭의 이름을 어떻게 가져올지 정의
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(8.dp),
           verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        tabItems.forEach { tab ->
            TabItem(
                name = getDisplayName(tab),
                onClick = { onTabSelected(tab) },
                isSelected = tab == selectedTab
            )
        }
    }
}


@Composable
fun TabItem(
    name: String,
    onClick: () -> Unit,
    isSelected: Boolean
){
    Text(
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) EveryLoLTheme.color.grayScale900 else EveryLoLTheme.color.grayScale1000)
            .clickable(onClick = onClick)
            .padding(8.dp),
        text = name,
        style = EveryLoLTheme.typography.body03,
        color = if (isSelected) EveryLoLTheme.color.white200 else EveryLoLTheme.color.community600,
        textAlign = TextAlign.Center
    )
}