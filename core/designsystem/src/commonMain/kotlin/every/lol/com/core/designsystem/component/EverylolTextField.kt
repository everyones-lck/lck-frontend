package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.designsystem.generated.resources.Res
import everylol.core.designsystem.generated.resources.ic_back

@Composable
fun EverylolTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    onDone: (() -> Unit)? = null,
    status: Int = 0
) {
    val isNotEmpty = value.isNotBlank()
    val keyboardController = LocalSoftwareKeyboardController.current

    val borderColor = when (status) {
        1 -> EveryLoLTheme.color.semanticCheck
        2 -> EveryLoLTheme.color.semanticWarning
        else -> EveryLoLTheme.color.grayScale300
    }

    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            if (it.length <= 10) onValueChange(it)
        },
        maxLines = 1,
        singleLine = true,
        textStyle = EveryLoLTheme.typography.subtitle02.copy(
            color = EveryLoLTheme.color.grayScale100
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
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
                        color = EveryLoLTheme.color.newBg,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (!isNotEmpty) {
                        hint?.let { h ->
                            Text(
                                text = h,
                                style = EveryLoLTheme.typography.subtitle02,
                                color = EveryLoLTheme.color.grayScale800
                            )
                        }
                    }
                    innerTextField()
                }
            }
        }
    )
}