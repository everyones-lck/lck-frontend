package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.designsystem.generated.resources.Res
import everylol.core.designsystem.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource

@Composable
fun EverylolBottomInputBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    onDone: (() -> Unit)? = null
) {
    val isNotEmpty = value.isNotEmpty()
    val keyboardController = LocalSoftwareKeyboardController.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = EveryLoLTheme.color.grayScale100,
        backgroundColor = EveryLoLTheme.color.grayScale100.copy(alpha = 0.4f)
    )

    val emptyTextToolbar = object : TextToolbar {
        override val status: TextToolbarStatus = TextToolbarStatus.Hidden
        override fun hide() {}
        override fun showMenu(
            rect: Rect,
            onCopyRequested: (() -> Unit)?,
            onPasteRequested: (() -> Unit)?,
            onCutRequested: (() -> Unit)?,
            onSelectAllRequested: (() -> Unit)?
        ) {}
    }

    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(EveryLoLTheme.color.newBg)
                .padding(16.dp, 0.dp, 16.dp, 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides customTextSelectionColors,
                LocalTextToolbar provides emptyTextToolbar
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = EveryLoLTheme.typography.pretendardBody02.copy(EveryLoLTheme.color.white200),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send //Todo: 보내기로 수정
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onDone?.invoke()
                        }
                    ),
                    cursorBrush = SolidColor(EveryLoLTheme.color.grayScale100),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 44.dp)
                                .background(
                                    color = EveryLoLTheme.color.grayScale1000,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp,14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (!isNotEmpty) {
                                hint?.let { h ->
                                    Text(
                                        text = h,
                                        style = EveryLoLTheme.typography.pretendardBody02,
                                        color = EveryLoLTheme.color.community600
                                    )
                                }
                            }
                            innerTextField()
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = EveryLoLTheme.color.grayScale1000,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            enabled = isNotEmpty,
                            onClick = {
                                keyboardController?.hide()
                                onDone?.invoke()
                            }
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_send),
                        contentDescription = "보내기",
                        tint = if (isNotEmpty) EveryLoLTheme.color.white200 else EveryLoLTheme.color.grayScale800
                    )
                }
            }
        }
    }
}