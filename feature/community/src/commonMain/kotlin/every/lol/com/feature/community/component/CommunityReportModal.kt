package every.lol.com.feature.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.community.generated.resources.Res
import everylol.feature.community.generated.resources.ic_dropdown_down
import everylol.feature.community.generated.resources.ic_dropdown_up
import org.jetbrains.compose.resources.painterResource


@Composable
fun CommunityReportModal (
    reportType: String,
    value: String?,
    confirmText: String? = "신고",
    onReport: () -> Unit = {},
    dismissText: String? = "취소",
    onDismiss: () -> Unit = {},
    modifier : Modifier = Modifier,
    onValueChange : (String) -> Unit = {},
    reasons: List<String> = listOf(
        "혐오, 차별, 생명경시, 욕설, 불쾌한 표현",
        "청소년에게 유해한 표현",
        "스팸 홍보, 도배 메시지",
        "음란물",
        "개인정보 노출",
        "명예훼손 및 사생활 침해",
        "불법 정보 포함",
        "불법 촬영물 포함",
        "기타"
    )
) {
    var internalValue by remember { mutableStateOf("") }
    val displayValue = if (value.isNullOrEmpty()) internalValue else value
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.grayScale900.copy(alpha = 0.8f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 28.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(EveryLoLTheme.color.grayScale100)
                .padding(24.dp, 24.dp, 24.dp, 20.dp)
                .clickable(enabled = false) { },
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "${reportType}을 신고하시겠습니까?",
                style = EveryLoLTheme.typography.subtitle02,
                color = EveryLoLTheme.color.grayScale1000
            )
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(EveryLoLTheme.color.grayScale200)
                        .clickable { expanded = !expanded }
                        .padding(16.dp, 12.dp, 16.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayValue.ifEmpty { "사유 선택하기" },
                        style = EveryLoLTheme.typography.body01,
                        color = if (displayValue == "") EveryLoLTheme.color.community600 else EveryLoLTheme.color.gray800
                    )
                    Icon(
                        painter = if (expanded) painterResource(Res.drawable.ic_dropdown_up) else painterResource(
                            Res.drawable.ic_dropdown_down
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).padding(6.dp),
                        tint = EveryLoLTheme.color.gray800
                    )
                }
                if (expanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 168.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EveryLoLTheme.color.grayScale200),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(vertical = 20.dp)
                    ) {
                        itemsIndexed(reasons) { index, reason ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        internalValue = reason
                                        onValueChange(reason)
                                        expanded = false
                                    }
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = reason,
                                    style = EveryLoLTheme.typography.subtitle03,
                                    color = EveryLoLTheme.color.grayScale800
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                dismissText?.let {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale400)
                            .clickable(
                                onClick = onDismiss
                            )
                            .padding(12.dp),
                        text = dismissText,
                        textAlign = TextAlign.Center,
                        style = EveryLoLTheme.typography.heading01,
                        color = EveryLoLTheme.color.gray800
                    )
                }
                confirmText?.let {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale800)
                            .clickable(
                                onClick = onReport
                            )
                            .padding(12.dp),
                        text = confirmText,
                        textAlign = TextAlign.Center,
                        style = EveryLoLTheme.typography.heading01,
                        color = EveryLoLTheme.color.grayScale400
                    )
                }
            }
        }
    }
}