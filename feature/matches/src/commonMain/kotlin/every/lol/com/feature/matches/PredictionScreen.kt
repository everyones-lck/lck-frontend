package every.lol.com.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.MatchCardModel
import every.lol.com.core.model.MatchStatus
import every.lol.com.feature.matches.component.CompactMatchCard
import every.lol.com.feature.matches.component.MatchesHeaderBar
import every.lol.com.feature.matches.component.PogSection
import every.lol.com.feature.matches.component.PogSectionMode
import every.lol.com.feature.matches.component.PogVoteItem
import every.lol.com.feature.matches.component.PredictionTeam
import every.lol.com.feature.matches.component.PredictionVoteSection
import every.lol.com.feature.matches.model.MatchIntent
import every.lol.com.feature.matches.model.MatchUiState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun PredictionRoute(
    matchId: Long,
    viewModel: MatchesViewModel = koinViewModel(MatchesViewModel::class),
    onBackClick: () -> Unit,
    onResultClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.onIntent(MatchIntent.ClickPrediction(matchId))
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(MatchIntent.LoadMatches)
        }
    }

    PredictionScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onResultClick = onResultClick,
        onVoteTeam = { teamId ->
            //
        },
        onSelectPog = { setIndex, playerId ->
            //
        }
    )
}

@Composable
fun PredictionScreen(
    uiState: MatchUiState,
    innerPadding: PaddingValues = PaddingValues(),
    onBackClick: () -> Unit,
    onResultClick: () -> Unit,
    onVoteTeam: (Long) -> Unit,
    onSelectPog: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val state = uiState as? MatchUiState.Prediction ?: return

    var voteItems by remember(state.setPogData) {
        mutableStateOf<List<PogVoteItem>>(
            state.setPogData?.mapIndexed { index, setDetail ->
                PogVoteItem(
                    title = if (index == (state.setPogData.size - 1)) "POM을 선택해주세요" else "${setDetail.setIndex}세트 POG를 선택해주세요",
                    options = setDetail.candidates.map { it.playerName } + "해당없음",
                    isExpanded = index == 0,
                    selectedOption = null
                )
            } ?: emptyList()
        )
    }

    var pogSectionMode by remember { mutableStateOf(PogSectionMode.VOTING) }

    val savedTexts = voteItems.mapIndexed { index, item ->
        val prefix = if (index == voteItems.lastIndex) "POM" else "${index + 1}세트"
        "$prefix : ${item.selectedOption ?: "선수이름"}"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.newBg)
    ) {
        MatchesHeaderBar(
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                state.matchData?.let { data ->
                    CompactMatchCard(
                        item = MatchCardModel(
                            matchId = data.matchId,
                            team1Name = data.team1.teamName,
                            team2Name = data.team2.teamName,
                            team1Id = data.team1.teamId,
                            team2Id = data.team2.teamId,
                            matchStatus = MatchStatus.SCHEDULED,
                            matchDate = state.matchDate,
                            seasonName = state.seasonName,
                            groupName = state.groupName,
                            roundName = state.roundName,
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                state.matchData?.let { data ->
                    PredictionVoteSection(
                        title = "승부예측",
                        matchStatus = MatchStatus.SCHEDULED,
                        team1Name = data.team1.teamName,
                        team2Name = data.team2.teamName,
                        selectedTeam = when (data.myVotedTeamId) {
                            data.team1.teamId.toLong() -> PredictionTeam.TEAM1
                            data.team2.teamId.toLong() -> PredictionTeam.TEAM2
                            else -> null
                        },
                        onLeftClick = { onVoteTeam(data.team1.teamId.toLong()) },
                        onRightClick = { onVoteTeam(data.team2.teamId.toLong()) }
                    )
                }
            }

            item {
                val currentMode = when {
                    state.setPogData.isNullOrEmpty() -> PogSectionMode.WAITING
                    pogSectionMode == PogSectionMode.SAVED -> PogSectionMode.SAVED
                    else -> PogSectionMode.VOTING
                }
                PogSection(
                    title = "나만의 POG / POM",
                    mode = currentMode,
                    voteItems = voteItems,
                    savedItems = savedTexts,
                    onToggleItem = { clickedIndex ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == clickedIndex) item.copy(isExpanded = !item.isExpanded)
                            else item.copy(isExpanded = false)
                        }
                    },
                    onSelectOption = { itemIndex, option ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == itemIndex) {
                                val playerId = state.setPogData?.get(itemIndex)?.candidates
                                    ?.find { it.playerName == option }?.playerId ?: -1

                                onSelectPog(state.setPogData?.get(itemIndex)?.setIndex ?: 0, playerId)

                                item.copy(selectedOption = option, isExpanded = false)
                            } else item
                        }
                    },
                    onSaveClick = {
                        pogSectionMode = PogSectionMode.SAVED
                    },
                    onResultClick = onResultClick
                )
            }
        }
    }
}