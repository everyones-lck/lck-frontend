package every.lol.com.feature.intro.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.model.TosType
import every.lol.com.core.ui.ext.everylolDefault
import everylol.feature.intro.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupBottomSheet(
    tosList: List<TosType>,
    onNavigateToTermDetail: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFixed: Boolean = true
) {
    val checkedStates = remember {
        mutableStateListOf<Boolean>().apply {
            if (isEmpty()) addAll(List(tosList.size) { false })
        }
    }

    val isEverythingSelected by remember {
        derivedStateOf {
            if (checkedStates.size >= 2) {
                checkedStates.all { it == true }
            } else {
                false
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { if (isFixed) it != SheetValue.Hidden else true }
    )

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = { if (!isFixed) onDismissRequest() },
        sheetState = sheetState,
        containerColor = EveryLoLTheme.color.grayScale1000,
        scrimColor = EveryLoLTheme.color.grayScale900.copy(alpha = 0.8f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null,
        properties = ModalBottomSheetProperties(shouldDismissOnBackPress = !isFixed)
    ) {
        EveryLoLTheme {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .padding(top = 48.dp, bottom = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Every.LOL을 하려면 동의가 필요해요",
                        style = EveryLoLTheme.typography.heading01,
                        color = EveryLoLTheme.color.grayScale200
                    )

                    // 모두 동의 Row
                    AgreementRow(
                        text = "모두 동의",
                        isSelected = isEverythingSelected,
                        onCheckClick = {
                            val target = !isEverythingSelected
                            for (i in checkedStates.indices) {
                                checkedStates[i] = target
                            }
                        }
                    )

                    // 개별 약관 리스트
                    tosList.forEachIndexed { index, term ->
                        AgreementRow(
                            text = term.title,
                            isSelected = checkedStates[index],
                            captionTitle = "세부 정보 보기",
                            onCheckClick = {
                                checkedStates[index] = !checkedStates[index]
                            },
                            onCaptionClick = {
                                onDismissRequest()
                                onNavigateToTermDetail(term.id)
                            }
                        )
                    }
                }

                EverylolButton(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .fillMaxWidth(),
                    text = "확인",
                    enabled = isEverythingSelected,
                    onClick = {
                        if (isEverythingSelected) onConfirmClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun AgreementRow(
    text: String,
    isSelected: Boolean,
    captionTitle: String? = null,
    onCheckClick: () -> Unit,
    onCaptionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCheckClick
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(
                if (isSelected) Res.drawable.ic_radio_button_selected
                else Res.drawable.ic_radio_button_un_selected
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(16.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = text,
                style = EveryLoLTheme.typography.subtitle02,
                color = EveryLoLTheme.color.grayScale200
            )
            if (captionTitle != null) {
                Text(
                    text = captionTitle,
                    style = EveryLoLTheme.typography.subtitle04,
                    color = EveryLoLTheme.color.grayScale700,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onCaptionClick() }
                )
            }
        }
    }
}