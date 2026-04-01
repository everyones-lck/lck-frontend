package every.lol.com.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeNewsDetail
import kotlinx.coroutines.delay

@Composable
fun NewsBannerRow(
    newsList: HomeNews?=null,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    val news = newsList?.newsList ?: emptyList()

    val pagerState = rememberPagerState(pageCount = { news.size })

    LaunchedEffect(key1 = news) {
        if (news.isNotEmpty()) {
            while (true) {
                delay(3000)
                val nextPage = (pagerState.currentPage + 1) % news.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "오늘의 LCK 소식",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.grayScale800,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .height(112.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 32.dp
        ) { page ->
            NewsBanner(
                banners = news[page],
                modifier = Modifier.fillMaxWidth(),
                onBannerClick = onClick
            )
        }
    }
}


@Composable
fun NewsBanner(
    banners: HomeNewsDetail,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onBannerClick(banners.link) } //딥링크 구현
    ){
        AsyncImage(
            model = banners.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        0.0f to Color.Black.copy(0f),
                        0.5f to Color.Black.copy(0.2f),
                        1f to Color.Black.copy(0.8f)
                    )
                )
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = banners.press,
                style = EveryLoLTheme.typography.caption01,
                color = EveryLoLTheme.color.white200,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = banners.publishedAt,
                style = EveryLoLTheme.typography.caption01,
                color = EveryLoLTheme.color.white200,
            )
            Text(
                text = banners.title,
                style = EveryLoLTheme.typography.subtitle03,
                color = EveryLoLTheme.color.white200,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.66f)
            )
        }
    }
}