package every.lol.com.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.matches.component.MatchesHeaderBar
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_ranking_five
import everylol.feature.matches.generated.resources.ic_ranking_four
import everylol.feature.matches.generated.resources.ic_ranking_one
import everylol.feature.matches.generated.resources.ic_ranking_three
import everylol.feature.matches.generated.resources.ic_ranking_two
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class ResultTabItem(
    val title: String
)

data class ResultRankItem(
    val rank: Int,
    val name: String
)
@Composable
fun LiveResultScreen(
    state: MatchUiState.LiveResult,
    innerPadding: PaddingValues = PaddingValues(),
    onBackClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        ResultTabItem("1세트"),
        ResultTabItem("2세트"),
        ResultTabItem("3세트"),
        ResultTabItem("4세트"),
        ResultTabItem("5세트"),
        ResultTabItem("POM")
    )

    val dummyRankItems = listOf(
        ResultRankItem(1, "나예은"),
        ResultRankItem(2, "나예은"),
        ResultRankItem(3, "나예은"),
        ResultRankItem(4, "나예은"),
        ResultRankItem(5, "나예은")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.newBg)
    ) {
        MatchesHeaderBar(
            title = "실시간 결과 보기",
            onBackClick = onBackClick
        )

        ResultTabRow(
            tabs = tabs,
            selectedTabIndex = state.selectedTabIndex,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyRankItems) { item ->
                ResultRankCard(item = item)
            }
        }
    }
}

@Composable
private fun ResultTabRow(
    tabs: List<ResultTabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(tabs) { index, tab ->
            val isSelected = selectedTabIndex == index

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        if (isSelected) {
                            EveryLoLTheme.color.grayScale900
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.title,
                    color = if (isSelected) {
                        EveryLoLTheme.color.white200
                    } else {
                        EveryLoLTheme.color.grayScale600
                    },
                    style = EveryLoLTheme.typography.body03
                )
            }
        }
    }
}

@Composable
private fun ResultRankCard(
    item: ResultRankItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(getRankDrawable(item.rank)),
            contentDescription = "순위 ${item.rank}",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = item.name,
            color = getRankTextColor(item.rank),
            style = EveryLoLTheme.typography.body01
        )
    }
}

@Composable
private fun getRankTextColor(rank: Int): Color {
    return when (rank) {
        1 -> EveryLoLTheme.color.grayScale100
        2 -> EveryLoLTheme.color.grayScale300
        3 -> EveryLoLTheme.color.grayScale600
        4 -> EveryLoLTheme.color.grayScale700
        5 -> EveryLoLTheme.color.grayScale800
        else -> EveryLoLTheme.color.grayScale200
    }
}

private fun getRankDrawable(rank: Int): DrawableResource {
    return when (rank) {
        1 -> Res.drawable.ic_ranking_one
        2 -> Res.drawable.ic_ranking_two
        3 -> Res.drawable.ic_ranking_three
        4 -> Res.drawable.ic_ranking_four
        5 -> Res.drawable.ic_ranking_five
        else -> Res.drawable.ic_ranking_five
    }
}