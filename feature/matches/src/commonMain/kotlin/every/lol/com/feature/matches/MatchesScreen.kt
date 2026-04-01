package every.lol.com.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.matches.component.CompactMatchDownCard
import every.lol.com.feature.matches.component.DetailMatchCard
import every.lol.com.feature.matches.component.TopBar
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun MatchesRoute(
    innerPadding: PaddingValues = PaddingValues(),
    viewModel: MatchesViewModel = koinViewModel(MatchesViewModel::class),
    onPredictionClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MatchUiState.Matches -> {
            MatchesScreen(
                innerPadding = innerPadding,
                state = state,
                onPredictionClick = { matchId ->
                    viewModel.onIntent(MatchIntent.ClickPrediction(matchId))
                    onPredictionClick(matchId)
                },
                onToggleMatchCard = { index ->
                    viewModel.onIntent(MatchIntent.ToggleMatchCard(index))
                }
            )
        }

        is MatchUiState.LiveResult -> {
            LiveResultScreen(
                state = state,
                innerPadding = innerPadding,
                onBackClick = {
                    viewModel.onIntent(MatchIntent.BackToPrediction)
                },
                onTabSelected = { index ->
                    viewModel.onIntent(MatchIntent.SelectLiveResultTab(index))
                }
            )
        }

        MatchUiState.Loading -> {

        }
        else -> {}
    }
}
@Composable
fun MatchesScreen(
    state: MatchUiState.Matches,
    innerPadding: PaddingValues = PaddingValues(),
    onPredictionClick: (Long) -> Unit,
    onToggleMatchCard: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.newBg)
            .padding(bottom = innerPadding.calculateBottomPadding() + 24.dp)
    ) {
        TopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(state.matches) { index, item ->
                val isExpanded = state.expandedIndex == index

                if (isExpanded) {
                    DetailMatchCard(
                        item = item,
                        onPredictionClick = {
                            onPredictionClick(item.matchId)
                        },
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onToggleMatchCard(index)
                        }
                    )
                } else {
                    CompactMatchDownCard(
                        item = item,
                        modifier = Modifier.clickable {
                            onToggleMatchCard(index)
                        }
                    )
                }
            }
        }
    }
}