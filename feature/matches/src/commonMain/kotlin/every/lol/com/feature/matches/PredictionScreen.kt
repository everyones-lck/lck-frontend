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
import every.lol.com.core.model.MatchPogCandidate
import every.lol.com.core.model.MatchStatus
import every.lol.com.core.model.SetPogVoteItem
import every.lol.com.feature.matches.component.CompactMatchCard
import every.lol.com.feature.matches.component.IncompletePogVoteDialog
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

    PredictionScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onResultClick = onResultClick,
        onVoteTeam = { teamId ->
            viewModel.onIntent(
                MatchIntent.SubmitPredictionVote(
                    matchId = matchId,
                    teamId = teamId
                )
            )
        },
        onSavePogVotes = { setVotes, pomPlayerId ->
            viewModel.onIntent(
                MatchIntent.SubmitPogVotes(
                    matchId = matchId,
                    setPogVotes = setVotes,
                    matchPogPlayerId = pomPlayerId
                )
            )
            viewModel.onIntent(
                MatchIntent.SubmitMatchPogVote(
                    matchId = matchId,
                    playerId = pomPlayerId
                )
            )
        }
    )
}

@Composable
fun PredictionScreen(
    uiState: MatchUiState,
    innerPadding: PaddingValues = PaddingValues(),
    onBackClick: () -> Unit,
    onResultClick: () -> Unit,
    onVoteTeam: (Int) -> Unit,
    onSavePogVotes: (List<SetPogVoteItem>, Long?) -> Unit,
    modifier: Modifier = Modifier
) {

    val state = uiState as? MatchUiState.Prediction ?: return

    var voteItems by remember(state.setPogData, state.matchPogData) {
        mutableStateOf(
            buildList {
                state.setPogData?.forEachIndexed { index, setDetail ->
                    add(
                        PogVoteItem(
                            title = "${setDetail.setIndex}세트 POG를 선택해주세요",
                            options = setDetail.candidates.map { it.playerName } + "해당없음",
                            isExpanded = index == 0,
                            selectedOption = null
                        )
                    )
                }

                state.matchPogData?.let { matchPog ->
                    add(
                        PogVoteItem(
                            title = "POM을 선택해주세요",
                            options = matchPog.candidates.map { it.playerName } + "해당없음",
                            isExpanded = false,
                            selectedOption = null
                        )
                    )
                }
            }
        )
    }

    var showIncompleteVoteDialog by remember { mutableStateOf(false) }

    val savedTexts = voteItems.mapIndexed { index, item ->
        val prefix = if (index == voteItems.lastIndex) "POM" else "${index + 1}세트"
        "$prefix : ${item.selectedOption ?: "투표 안 함"}"
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
                state.match?.let { card ->
                    CompactMatchCard(
                        item = card,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                val card = state.match
                val data = state.matchData

                if (card != null && data != null) {
                    PredictionVoteSection(
                        title = "승부예측",
                        matchStatus = card.matchStatus,
                        team1Name = data.team1.teamName,
                        team2Name = data.team2.teamName,
                        selectedTeam = when (data.myVotedTeamId) {
                            data.team1.teamId.toLong() -> PredictionTeam.TEAM1
                            data.team2.teamId.toLong() -> PredictionTeam.TEAM2
                            else -> null
                        },
                        isVoteEnabled = data.votable,
                        onLeftClick = {
                            onVoteTeam(data.team1.teamId)
                        },
                        onRightClick = {
                            onVoteTeam(data.team2.teamId)
                        }
                    )
                }
            }

            item {
                val currentMode = when {
                    state.setPogData.isNullOrEmpty() -> PogSectionMode.WAITING
                    state.isPogSaved -> PogSectionMode.SAVED
                    else -> PogSectionMode.VOTING
                }
                PogSection(
                    title = "나만의 POG / POM",
                    mode = currentMode,
                    voteItems = voteItems,
                    savedItems = savedTexts,
                    isSaveEnabled = !state.isPogSaved,
                    onToggleItem = { clickedIndex ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == clickedIndex) item.copy(isExpanded = !item.isExpanded)
                            else item.copy(isExpanded = false)
                        }
                    },
                    onSelectOption = { itemIndex, option ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == itemIndex) {
                                item.copy(
                                    selectedOption = option,
                                    isExpanded = false
                                )
                            } else item
                        }
                    },
                    onSaveClick = {
                        val setVotes = state.setPogData?.mapIndexed { index, setDetail ->
                            val selectedOption = voteItems.getOrNull(index)?.selectedOption

                            val playerId: Long? = when (selectedOption) {
                                null, "해당없음" -> null
                                else -> setDetail.candidates
                                    .find { it.playerName == selectedOption }
                                    ?.playerId
                                    ?.toLong()
                            }

                            SetPogVoteItem(
                                setIndex = setDetail.setIndex,
                                playerId = playerId
                            )
                        }.orEmpty()

                        val pomSelectedOption = voteItems.lastOrNull()?.selectedOption
                        val pomPlayerId: Long? = when (pomSelectedOption) {
                            null, "해당없음" -> null
                            else -> state.matchPogData?.candidates
                                ?.find { it.playerName == pomSelectedOption }
                                ?.playerId
                                ?.toLong()
                        }

                        val isAllSelected = voteItems.all { it.selectedOption != null }

                        if (isAllSelected) {
                            onSavePogVotes(setVotes, pomPlayerId)
                        } else {
                            showIncompleteVoteDialog = true
                        }
                    },
                    onResultClick = onResultClick
                )
            }
        }
    }
    if (showIncompleteVoteDialog) {
        IncompletePogVoteDialog(
            voteItems = voteItems,
            onDismiss = {
                showIncompleteVoteDialog = false
            },
            onConfirmSave = {
                val setVotes = state.setPogData?.mapIndexed { index, setDetail ->
                    val selectedOption = voteItems.getOrNull(index)?.selectedOption

                    val playerId: Long? = when (selectedOption) {
                        null, "해당없음" -> null
                        else -> setDetail.candidates
                            .find { it.playerName == selectedOption }
                            ?.playerId
                            ?.toLong()
                    }

                    SetPogVoteItem(
                        setIndex = setDetail.setIndex,
                        playerId = playerId
                    )
                }.orEmpty()

                val pomSelectedOption = voteItems.lastOrNull()?.selectedOption
                val pomPlayerId: Long? = when (pomSelectedOption) {
                    null, "해당없음" -> null
                    else -> state.matchPogData?.candidates
                        ?.find { it.playerName == pomSelectedOption }
                        ?.playerId
                        ?.toLong()
                }

                showIncompleteVoteDialog = false
                onSavePogVotes(setVotes, pomPlayerId)
            }
        )
    }
}