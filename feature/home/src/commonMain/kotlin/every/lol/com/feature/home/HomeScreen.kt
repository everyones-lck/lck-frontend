package every.lol.com.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.LckRankingSection
import every.lol.com.feature.home.component.MatchCardRow
import every.lol.com.feature.home.component.MatchNoticeBanner
import every.lol.com.feature.home.component.NewsBannerRow
import every.lol.com.feature.home.component.TopBar
import every.lol.com.feature.home.model.HomeIntent
import every.lol.com.feature.home.model.HomeUiState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun HomeRoute(
    onNavigateToMypage: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(HomeViewModel ::class)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeState = uiState as? HomeUiState.Home

/*    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadInitial)
    }*/

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is HomeEvent.ShowToast -> {  }
                else -> {}
            }
        }
    }

    println(homeState)
    if(homeState == null){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(EveryLoLTheme.color.newBg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = EveryLoLTheme.color.grayScale200)
        }

    }else {
        HomeScreen(
            state = uiState,
            onNavigateToMypage = onNavigateToMypage,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    innerPadding: PaddingValues = PaddingValues(),
    onNavigateToMypage: () -> Unit,
    onIntent: (HomeIntent) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val homeState = state as? HomeUiState.Home
    var showMatchBanner by rememberSaveable { mutableStateOf(true) }
    val matchData = homeState?.matches
    val newsBanners = homeState?.news
    val currentAlerts = homeState?.alerts?.alerts ?: emptyList()
    val ranking = homeState?.ranking?.groups?.firstOrNull()?.teams ?: emptyList()
    val supportTeams = homeState?.supportTeam ?: emptyList()

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
                  onNavigateToMypage()
                }
            )
        }
        items(
            items = currentAlerts,
            key = { it.matchId }
        ) { alert ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MatchNoticeBanner(
                    message = alert.message,
                    onCloseClick = {
                        onIntent(HomeIntent.CloseAlarm(alert.matchId))
                    }
                )
            }
        }
        item {

            MatchCardRow(
                matchCards = matchData,
                matchRate = homeState?.matchRates ?: emptyMap(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
        if(newsBanners!=null){
            item {
                NewsBannerRow(
                    newsList = newsBanners,
                    onClick = { url ->
                        if (url.isNotBlank()) {
                            try {
                                uriHandler.openUri(url)
                            } catch (e: Exception) {

                            }
                        }
                    }
                )
            }
        }

        item {
            LckRankingSection(
                standings = ranking,
                supportTeams = supportTeams,
                modifier = Modifier.padding(top = 24.dp).padding(horizontal = 16.dp),
                onTeamClick = { teamId ->
                    // TODO
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
