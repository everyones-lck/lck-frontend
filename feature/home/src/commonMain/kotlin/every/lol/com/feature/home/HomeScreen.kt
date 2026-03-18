package every.lol.com.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.HomeBannerModel
import every.lol.com.core.model.LckStandingTeamModel
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.ui.component.DefaultScreen
import every.lol.com.feature.home.component.LckRankingSection
import every.lol.com.feature.home.component.MatchCard
import every.lol.com.feature.home.component.MatchNoticeBanner
import every.lol.com.feature.home.component.NewsBanner
import every.lol.com.feature.home.component.TopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import every.lol.com.core.designsystem.theme.EveryLoLTheme


@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(),
      onNavigateToMypage: () -> Unit
) {
    var showMatchBanner by rememberSaveable { mutableStateOf(true) }

    val dummyMatchCard = MatchCardModel(
        matchId = 1L,
        title = "2026 Road to MSI",
        team1Name = "HLE",
        team2Name = "Gen",
        matchName = "Baron Elder",
        roundName = "플레이오프 1라운드"
    )
    val newsBanners = listOf(
        HomeBannerModel(id = 1L, imageUrl = "", title = " "),
        HomeBannerModel(id = 2L, imageUrl = "", title = ""),
        HomeBannerModel(id = 3L, imageUrl = "", title = "")
    )
    val standings = listOf(
        LckStandingTeamModel(teamId = 6, rank = 6, teamName = "T1", rightText = "-"),
        LckStandingTeamModel(teamId = 1, rank = 1, teamName = "GEN", rightText = "-"),
        LckStandingTeamModel(teamId = 2, rank = 2, teamName = "KT", rightText = "-"),
        LckStandingTeamModel(teamId = 3, rank = 3, teamName = "HLE", rightText = "-"),
        LckStandingTeamModel(teamId = 4, rank = 4, teamName = "DN", rightText = "-"),
        LckStandingTeamModel(teamId = 5, rank = 5, teamName = "BFX", rightText = "-"),
        LckStandingTeamModel(teamId = 7, rank = 7, teamName = "DRX", rightText = "-"),
        LckStandingTeamModel(teamId = 8, rank = 8, teamName = "BNK", rightText = "-"),
        LckStandingTeamModel(teamId = 9, rank = 9, teamName = "NS", rightText = "-")
    )
    val myFavoriteTeamId = 6L

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.newBg),
        contentPadding = PaddingValues(
            bottom = innerPadding.calculateBottomPadding() + 24.dp
        )

    ) {
        item {
            TopBar(
                onProfileClick = {
                  onNavigateToMypage
                }
            )
        }
        item {
            if (showMatchBanner) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    MatchNoticeBanner(
                        message = "오늘은 Gen.G 경기가 있는 날이에요.",
                        onCloseClick = { showMatchBanner = false }
                    )
                }
            }
        }

        item {
            MatchCard(
                matchCard = dummyMatchCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                team1Progress = 0.28f,
                team2Progress = 0.72f,
                onClick = { }
            )
        }

        item {
            NewsBanner(
                banners = newsBanners,
                modifier = Modifier.padding(top = 24.dp),
                onBannerClick = { bannerId ->
                    // Todo: 배너 클릭 처리
                }
            )
        }

        item {
            LckRankingSection(
                standings = standings,
                favoriteTeamId = myFavoriteTeamId,
                modifier = Modifier.padding(top = 24.dp),
                onTeamClick = { teamId ->
                    // TODO
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewHome(){
    EveryLoLTheme {
        HomeScreen()
    }