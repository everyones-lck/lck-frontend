package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun EverylolTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    onDone: (() -> Unit)? = null,
    status: Int = 0,
    maxLine: Int = 1,
    community: Boolean = false
) {
    val isNotEmpty = value.isNotEmpty()
    val keyboardController = LocalSoftwareKeyboardController.current

    val currentBorderColor = if (community) {
        EveryLoLTheme.color.grayScale1000
    } else {
        when (status) {
            1 -> EveryLoLTheme.color.semanticCheck
            2 -> EveryLoLTheme.color.semanticWarning
            3 -> EveryLoLTheme.color.grayScale200
            else -> EveryLoLTheme.color.grayScale300
        }
    }

    val currentTextStyle = if (community) {
        EveryLoLTheme.typography.pretendardBody02.copy(color = EveryLoLTheme.color.white200)
    } else {
        EveryLoLTheme.typography.subtitle02.copy(color = EveryLoLTheme.color.grayScale100)
    }

    val currentHintStyle = if (community) {
        EveryLoLTheme.typography.pretendardBody02.copy(color = EveryLoLTheme.color.black900)
    } else {
        EveryLoLTheme.typography.subtitle02.copy(color = EveryLoLTheme.color.grayScale800)
    }

    val backgroundColor = if (community) {
        EveryLoLTheme.color.grayScale1000
    } else {
        EveryLoLTheme.color.newBg
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = EveryLoLTheme.color.grayScale100,
        backgroundColor = EveryLoLTheme.color.grayScale100.copy(alpha = 0.4f)
    )

    val emptyTextToolbar = object : TextToolbar {
        override val status: TextToolbarStatus = TextToolbarStatus.Hidden
        override fun hide() {}
        override fun showMenu(
            rect: androidx.compose.ui.geometry.Rect,
            onCopyRequested: (() -> Unit)?,
            onPasteRequested: (() -> Unit)?,
            onCutRequested: (() -> Unit)?,
            onSelectAllRequested: (() -> Unit)?
        ) {}
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
        LocalTextToolbar provides emptyTextToolbar
    ) {
        BasicTextField(
            modifier = modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            maxLines = maxLine,
            singleLine = maxLine == 1,
            textStyle = currentTextStyle,
            keyboardOptions = KeyboardOptions(
                imeAction = if (maxLine > 1) ImeAction.Default else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDone?.invoke()
                }
            ),
            cursorBrush = SolidColor(EveryLoLTheme.color.grayScale100),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = currentBorderColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = if (maxLine > 1) Alignment.Top else Alignment.CenterVertically,
                    ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = if (maxLine > 1) Alignment.TopStart else Alignment.CenterStart
                    ) {
                        if (!isNotEmpty) {
                            hint?.let { h ->
                                Text(
                                    text = h,
                                    style = currentHintStyle,
                                    color = currentHintStyle.color
                                )
                            }
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}