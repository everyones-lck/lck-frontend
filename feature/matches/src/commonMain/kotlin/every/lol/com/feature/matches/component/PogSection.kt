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
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.matches.generated.resources.Res
import everylol.feature.matches.generated.resources.ic_double_arrow_right
import everylol.feature.matches.generated.resources.ic_dropdown_down
import everylol.feature.matches.generated.resources.ic_dropdown_up
import everylol.feature.matches.generated.resources.ic_waiting_pog
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

                PogWaitingCard()
            }

            PogSectionMode.VOTING -> {
                PogVoteHeader(
                    title = title,
                    showSaveButton = true,
                    onSaveClick = onSaveClick
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
            }

            PogSectionMode.SAVED -> {
                PogSavedHeader(
                    title = title,
                    onResultClick = onResultClick
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    savedItems.forEach { text ->
                        PogSavedItemCard(text = text)
                    }
                }
            }
        }
    }
}

@Composable
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
}

@Composable
fun PogVoteHeader(
    title: String,
    showSaveButton: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = EveryLoLTheme.color.grayScale600,
            style = EveryLoLTheme.typography.subtitle03
        )

        if (showSaveButton) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(EveryLoLTheme.color.grayScale900)
                    .clickable(onClick = onSaveClick)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "저장하기",
                    color = EveryLoLTheme.color.grayScale600,
                    style = EveryLoLTheme.typography.subtitle03
                )
            }
        }
    }
}

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
private fun PogSavedHeader(
    title: String,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = EveryLoLTheme.color.grayScale600,
            style = EveryLoLTheme.typography.subtitle03
        )

        Row(
            modifier = Modifier.clickable(onClick = onResultClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "실시간 결과 보기",
                color = EveryLoLTheme.color.white200,
                style = EveryLoLTheme.typography.subtitle03
            )

            Icon(
                painter = painterResource(Res.drawable.ic_double_arrow_right),
                contentDescription = "실시간 결과 보기",
                tint = EveryLoLTheme.color.grayScale200,
                modifier = Modifier.size(width = 12.dp, height = 12.dp)
            )
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
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = EveryLoLTheme.color.grayScale600,
            style = EveryLoLTheme.typography.body01
        )
    }
}