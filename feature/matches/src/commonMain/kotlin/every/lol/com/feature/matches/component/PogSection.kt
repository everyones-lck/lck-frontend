package every.lol.com.feature.matches.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.EmptyContent
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_dropdown_down
import everylol.feature.matches.generated.resources.ic_dropdown_up
import org.jetbrains.compose.resources.painterResource

enum class PogSectionMode {
    WAITING,
    VOTING,
    SAVED
}

data class PogVoteItem(
    val title: String,
    val options: List<String>,
    val selectedOption: String? = null,
    val isExpanded: Boolean = false
)

@Composable
fun PogSection(
    title: String,
    mode: PogSectionMode,
    voteItems: List<PogVoteItem> = emptyList(),
    savedItems: List<String> = emptyList(),
    isSaveEnabled: Boolean = true,
    onToggleItem: (Int) -> Unit = {},
    onSelectOption: (itemIndex: Int, option: String) -> Unit = { _, _ -> },
    onSaveClick: () -> Unit = {},
    onResultClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (mode) {
            PogSectionMode.WAITING -> {
                Text(
                    text = title,
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )
                EmptyContent("unfinished", "경기 종료 후,\n 나만의 POG/POM을 뽑아보세요")
            }
            PogSectionMode.VOTING -> {
                Text(
                    text = title,
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    voteItems.forEachIndexed { index, item ->
                        PogDropdownItem(
                            title = item.title,
                            options = item.options,
                            selectedOption = item.selectedOption,
                            isExpanded = item.isExpanded,
                            onToggle = { onToggleItem(index) },
                            onSelectOption = { option ->
                                onSelectOption(index, option)
                            }
                        )
                    }
                }

                PogBottomButtons(
                    isSaved = false,
                    isSaveEnabled = isSaveEnabled,
                    onSaveClick = onSaveClick,
                    onResultClick = onResultClick
                )
            }
            PogSectionMode.SAVED -> {
                Text(
                    text = title,
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    savedItems.forEach { text ->
                        PogSavedItemCard(text = text)
                    }
                }

                PogBottomButtons(
                    isSaved = true,
                    isSaveEnabled = false,
                    onSaveClick = onSaveClick,
                    onResultClick = onResultClick
                )
            }
        }
    }
}

/*@Composable
fun PogWaitingCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(328.dp)
            .height(284.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EveryLoLTheme.color.grayScale1000),
        contentAlignment = Alignment.Center
    ) {
        EmptyContent("unfinished", "경기 종료 후,\n 나만의 POG/POM을 뽑아보세요")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_waiting_pog),
                contentDescription = "POG POM 대기",
                tint = EveryLoLTheme.color.grayScale600,
                modifier = Modifier.size(58.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "경기 종료 후,",
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )

                Text(
                    text = "나만의 POG/POM을 뽑아보세요",
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )
            }
        }
    }
}*/

@Composable
fun PogDropdownItem(
    title: String,
    options: List<String>,
    selectedOption: String?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(328.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EveryLoLTheme.color.grayScale900)
                .clickable(onClick = onToggle)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption ?: title,
                    color = if (selectedOption != null) {
                        EveryLoLTheme.color.white200
                    } else {
                        EveryLoLTheme.color.grayScale800
                    },
                    style = EveryLoLTheme.typography.body01
                )

                Icon(
                    painter = painterResource(
                        if (isExpanded) Res.drawable.ic_dropdown_up
                        else Res.drawable.ic_dropdown_down
                    ),
                    contentDescription = if (isExpanded) "접기" else "펼치기",
                    tint = EveryLoLTheme.color.grayScale200,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (isExpanded) {
            PogOptionList(
                options = options,
                onSelectOption = onSelectOption
            )
        }
    }
}

@Composable
private fun PogOptionList(
    options: List<String>,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale900)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectOption(option) }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = option,
                    color = EveryLoLTheme.color.white200,
                    style = EveryLoLTheme.typography.body01
                )
            }
        }
    }
}

@Composable
private fun PogBottomButtons(
    isSaved: Boolean,
    isSaveEnabled: Boolean,
    onSaveClick: () -> Unit,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(328.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PogActionButton(
            text = if (isSaved) "저장완료" else "저장하기",
            backgroundColor = if (isSaved) {
                EveryLoLTheme.color.grayScale800
            } else {
                EveryLoLTheme.color.grayScale400
            },
            textColor = if (isSaved) {
                EveryLoLTheme.color.grayScale600
            } else {
                EveryLoLTheme.color.black900
            },
            enabled = isSaveEnabled,
            modifier = Modifier.weight(1f),
            onClick = onSaveClick
        )

        PogActionButton(
            text = "실시간 결과보기",
            backgroundColor = EveryLoLTheme.color.grayScale400,
            textColor = EveryLoLTheme.color.black900,
            enabled = true,
            modifier = Modifier.weight(1f),
            onClick = onResultClick
        )
    }
}

@Composable
private fun PogActionButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = EveryLoLTheme.typography.body03
        )
    }
}

@Composable
fun IncompletePogVoteDialog(
    voteItems: List<PogVoteItem>,
    onDismiss: () -> Unit,
    onConfirmSave: () -> Unit
) {
    val notSelectedTexts = voteItems.mapIndexedNotNull { index, item ->
        if (item.selectedOption != null) return@mapIndexedNotNull null

        if (index == voteItems.lastIndex) {
            "POM (투표 안 함)"
        } else {
            "${index + 1}세트 POG (투표 안 함)"
        }
    }

    val detailText = if (notSelectedTexts.isEmpty()) {
        ""
    } else {
        "모든 투표가 완료되지 않았습니다.\n${notSelectedTexts.joinToString(", ")}"
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .width(304.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(EveryLoLTheme.color.grayScale100)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "투표를 완료할까요?",
                        color = EveryLoLTheme.color.grayScale1000,
                        style = EveryLoLTheme.typography.subtitle02
                    )

                    Text(
                        text = detailText,
                        color = EveryLoLTheme.color.grayScale600,
                        style = EveryLoLTheme.typography.pretendardBody02
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale400)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "수정",
                            color = EveryLoLTheme.color.grayScale800,
                            style = EveryLoLTheme.typography.heading02
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale800)
                            .clickable(onClick = onConfirmSave),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "저장",
                            color = EveryLoLTheme.color.grayScale400,
                            style = EveryLoLTheme.typography.heading02
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PogSavedItemCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(328.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = EveryLoLTheme.color.grayScale100,
            style = EveryLoLTheme.typography.body01
        )
    }
}