package every.lol.com.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
import every.lol.com.feature.matches.component.PredictionResult
import every.lol.com.feature.matches.component.PredictionTeam
import every.lol.com.feature.matches.component.PredictionVoteSection

@Composable
fun PredictionScreen(
    matchId: Long,
    innerPadding: PaddingValues = PaddingValues(),
    onBackClick: () -> Unit,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pogSectionMode by remember { mutableStateOf(PogSectionMode.VOTING) }


    var voteItems by remember {
        mutableStateOf(
            listOf(
                PogVoteItem(
                    title = "1세트 POG를 선택해주세요",
                    options = listOf("text 1", "text 2", "text 3", "해당없음"),
                    isExpanded = true
                ),
                PogVoteItem(
                    title = "2세트 POG를 선택해주세요",
                    options = listOf("text 1", "text 2", "text 3", "해당없음")
                ),
                PogVoteItem(
                    title = "3세트 POG를 선택해주세요",
                    options = listOf("text 1", "text 2", "text 3", "해당없음")
                ),
                PogVoteItem(
                    title = "n세트 POG를 선택해주세요",
                    options = listOf("text 1", "text 2", "text 3", "해당없음")
                ),
                PogVoteItem(
                    title = "POM을 선택해주세요",
                    options = listOf("text 1", "text 2", "text 3", "해당없음","123","456")
                )
            )
        )
    }

    val savedTexts = voteItems.mapIndexed { index, item ->
        val prefix = if (index == voteItems.lastIndex) "POM" else "${index + 1}세트"
        "$prefix : ${item.selectedOption ?: "선수이름"}"
    }

    val dummyMatch = MatchCardModel(
        matchId = matchId,
        seasonName = "2026 Road to MSI",
        team1Name = "HLE",
        team2Name = "Gen",
        groupName = "Baron Elder",
        roundName = "플레이오프 1라운드",
        matchDate = "",
        matchStatus = MatchStatus.LIVE,
        team1Id = 1,
        team2Id = 2,
        team1VoteRate = 0.0,
        team2VoteRate = 0.0,
        totalVoteCount = 0,
        predictedWinnerTeamName = "",
    )

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
                CompactMatchCard(
                    item = dummyMatch,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                PredictionVoteSection(
                    title = "승부예측",
                    matchStatus = MatchStatus.FINISHED,
                    team1Name = "GEN",
                    team2Name = "T1",
                    selectedTeam = PredictionTeam.TEAM2,
                    predictionResult = PredictionResult.SUCCESS,
                    onLeftClick = {},
                    onRightClick = {}
                )
            }

            item {
                PogSection(
                    title = "나만의 POG / POM",
                    mode = pogSectionMode,
                    voteItems = voteItems,
                    savedItems = savedTexts,
                    onToggleItem = { clickedIndex ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == clickedIndex) {
                                item.copy(isExpanded = !item.isExpanded)
                            } else {
                                item.copy(isExpanded = false)
                            }
                        }
                    },
                    onSelectOption = { itemIndex, option ->
                        voteItems = voteItems.mapIndexed { index, item ->
                            if (index == itemIndex) {
                                item.copy(
                                    selectedOption = option,
                                    isExpanded = false
                                )
                            } else {
                                item
                            }
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