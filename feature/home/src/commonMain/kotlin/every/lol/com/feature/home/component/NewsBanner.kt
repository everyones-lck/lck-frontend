package every.lol.com.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.HomeNews
import every.lol.com.core.model.HomeNewsDetail
import everylol.feature.home.generated.resources.Res
import everylol.feature.home.generated.resources.img_news_default
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

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
    ) {
        Text(
            text = "오늘의 LCK 소식",
            style = EveryLoLTheme.typography.subtitle03,
            color = EveryLoLTheme.color.grayScale800,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (news.isEmpty()) {
            Image(
                painter = painterResource(Res.drawable.img_news_default),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(112.dp).padding(horizontal = 16.dp)
            )
        } else {
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
}

@Composable
fun NewsBanner(
    banners: HomeNewsDetail,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
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
                .background(Color.Black.copy(0.2f))
        )
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(12.dp, 7.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.Start
        ) {
            Box {
                Text(
                    text = "${banners.press} · ${banners.publishedAt}",
                    style = EveryLoLTheme.typography.label03.copy(
                        drawStyle = Stroke(
                            width = 0.2f,
                            join = StrokeJoin.Round
                        )
                    ),
                    color = EveryLoLTheme.color.gray800
                )
                Text(
                    text = "${banners.press} · ${banners.publishedAt}",
                    style = EveryLoLTheme.typography.label03,
                    color = EveryLoLTheme.color.white200
                )
            }
            Text(
                modifier = Modifier
                    .widthIn(max = 160.dp)
                    .background(EveryLoLTheme.color.black900)
                    .padding(4.dp),
                text = banners.title,
                style = EveryLoLTheme.typography.subtitle04,
                color = EveryLoLTheme.color.grayScale100,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}