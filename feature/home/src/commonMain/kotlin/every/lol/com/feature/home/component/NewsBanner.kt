package every.lol.com.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.HomeBannerModel

@Composable
fun NewsBanner(
    banners: List<HomeBannerModel>,
    modifier: Modifier = Modifier,
    onBannerClick: (Long) -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "오늘의 LCK 소식",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.grayScale800,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
        ) { page ->
            val banner = banners[page]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(EveryLoLTheme.color.grayScale800)
                    .clickable { onBannerClick(banner.id) }
            ) {
                // Todo: LCK 소식 API
            }
        }
    }
}