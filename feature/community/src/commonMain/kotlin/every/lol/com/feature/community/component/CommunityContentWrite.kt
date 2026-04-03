package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.feature.community.model.CommunityIntent
import every.lol.com.feature.community.model.CommunityUiState
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_x
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun CommunityContentWriteBlock(
    modifier: Modifier = Modifier,
    state: CommunityUiState.Write,
    onIntent: (CommunityIntent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val textLines = remember(state.content) { state.content.split("\n") }
    val focusRequesters = remember { List(100) { FocusRequester() } }

    var pendingFocusIndex by remember { mutableIntStateOf(-1) }

    val allItems = remember(textLines, state.selectedMedias) {
        val list = mutableListOf<String>()
        textLines.forEachIndexed { index, _ ->
            list.add("text_block_$index")
            state.selectedMedias.filter { it.order == index }.forEach { list.add(it.id) }
        }
        val assignedIds = list.toSet()
        state.selectedMedias.filter { it.id !in assignedIds }.forEach { list.add(it.id) }
        list
    }

    LaunchedEffect(textLines.size) {
        if (textLines.size > 1) {
            delay(30)
            focusRequesters.getOrNull(textLines.size - 1)?.requestFocus()
        }
    }

    LaunchedEffect(pendingFocusIndex) {
        if (pendingFocusIndex != -1) {
            delay(50)
            focusRequesters.getOrNull(pendingFocusIndex)?.requestFocus()
            pendingFocusIndex = -1
        }
    }

    ReorderableColumn(
        list = allItems,
        onSettle = { from, to ->
            val fromId = allItems.getOrNull(from) ?: ""
            if (state.selectedMedias.any { it.id == fromId }) {
                val nearestTextIndex = allItems.subList(0, (to + 1).coerceAtMost(allItems.size))
                    .filter { it.startsWith("text_block_") }
                    .lastOrNull()
                    ?.removePrefix("text_block_")?.toIntOrNull() ?: 0
                onIntent(CommunityIntent.UpdateMediaOrder(fromId, nearestTextIndex))
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 410.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveryLoLTheme.color.grayScale1000)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) { index, itemKey, isDragging ->
        key(itemKey) {
            ReorderableItem(modifier = Modifier.fillMaxWidth()) {
                if (itemKey.startsWith("text_block_")) {
                    val textIndex = itemKey.removePrefix("text_block_").toIntOrNull() ?: 0
                    val currentText = textLines.getOrNull(textIndex) ?: ""

                    ContentTextItem(
                        value = currentText,
                        focusRequester = focusRequesters.getOrNull(textIndex) ?: FocusRequester(),
                        onValueChange = { newText ->
                            val mutableLines = textLines.toMutableList()
                            if (textIndex < mutableLines.size) {
                                mutableLines[textIndex] = newText
                                onIntent(CommunityIntent.ChangeContent(mutableLines.joinToString("\n")))
                            }
                        },
                        onEnterPressed = {
                            val mutableLines = textLines.toMutableList()
                            mutableLines.add(textIndex + 1, "")
                            onIntent(CommunityIntent.ChangeContent(mutableLines.joinToString("\n")))
                            pendingFocusIndex = textIndex + 1
                        },
                        onBackspaceOnEmpty = {
                            if (textIndex > 0) {
                                focusRequesters[textIndex - 1].requestFocus()
                                coroutineScope.launch {
                                    delay(10)
                                    handleBackspace(textIndex, textLines, onIntent)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        hint = if (textIndex == 0 && state.content.isEmpty()) {
                            "- 사진은 최대 10장, 영상은 최대 2개 까지 첨부가능합니다\n" +
                                    "- 공백 포함 최대 2000자 작성가능합니다"
                        } else null
                    )
                } else {
                    val media = state.selectedMedias.find { it.id == itemKey }
                    media?.let {
                        ContentMediaItem(
                            media = it,
                            isDragging = isDragging,
                            onRemove = {
                                onIntent(
                                    CommunityIntent.RemoveMedia(
                                        state.selectedMedias.indexOf(
                                            it
                                        )
                                    )
                                )
                            },
                            modifier = Modifier.draggableHandle()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentTextItem(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onEnterPressed: () -> Unit,
    onBackspaceOnEmpty: () -> Unit,
    hint: String? = null,
    modifier: Modifier = Modifier
) {
    val textFieldValueState = remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    LaunchedEffect(value) {
        if (value != textFieldValueState.value.text) {
            textFieldValueState.value = textFieldValueState.value.copy(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        // 💡 엔터 키를 직접 가로채서 처리
                        if (event.key == Key.Enter) {
                            onEnterPressed()
                            return@onPreviewKeyEvent true
                        }
                        // 💡 백스페이스 처리
                        if (event.key == Key.Backspace && value.isEmpty()) {
                            onBackspaceOnEmpty()
                            return@onPreviewKeyEvent true
                        }
                    }
                    false
                }
                .focusRequester(focusRequester),
            value = textFieldValueState.value,
            onValueChange = { next->
                if (!next.text.contains("\n")) {
                    textFieldValueState.value = next
                    if (next.text != value) {
                        onValueChange(next.text)
                    }
                }
            },
            textStyle = EveryLoLTheme.typography.pretendardBody02.copy(color = EveryLoLTheme.color.white200),
            cursorBrush = SolidColor(EveryLoLTheme.color.grayScale100),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (textFieldValueState.value.text.isEmpty() && hint != null) {
                        Text(
                            text = hint,
                            style = EveryLoLTheme.typography.pretendardBody02.copy(color = EveryLoLTheme.color.black900)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun ContentMediaItem(
    media: CommunityUiState.MediaItem,
    isDragging: Boolean,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EveryLoLTheme.color.grayScale900)
        ) {
            AsyncImage(
                model = media.url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (media.isVideo) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "비디오는 글 등록 후 재생이 가능합니다",
                        style = EveryLoLTheme.typography.pretendardBody02,
                        color = EveryLoLTheme.color.grayScale100
                    )
                }
            }

            if (isDragging) {
                Box(modifier = Modifier.matchParentSize().background(Color.White.copy(alpha = 0.4f)))
            }

            // 삭제 버튼
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_x),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.White
                )
            }
        }
    }
}

private fun handleBackspace(index: Int, lines: List<String>, onIntent: (CommunityIntent) -> Unit) {
    val mutableLines = lines.toMutableList()
    if (index > 0) {
        val currentLine = mutableLines[index]
        val prevLine = mutableLines[index - 1]

        mutableLines[index - 1] = prevLine + currentLine
        mutableLines.removeAt(index)

        onIntent(CommunityIntent.ChangeContent(mutableLines.joinToString("\n")))
    }
}