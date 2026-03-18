package every.lol.com.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.mypage.generated.resources.Res
import everylol.feature.mypage.generated.resources.ic_arrow_bottom
import everylol.feature.mypage.generated.resources.ic_arrow_up
import org.jetbrains.compose.resources.painterResource

@Composable
fun WithdrawalReasonSection(
    modifier : Modifier = Modifier,
    value : String?= null,
    onValueChange : (String) -> Unit = {},
    reasons: List<String> = listOf(
        "원하는 기능이 없어요",
        "즐길 수 있는 콘텐츠가 부족해요",
        "서비스 이용이 어려워요",
        "더 좋은 서비스가 있어요",
        "더 이상 LCK에 관심이 없어요",
        "직접 작성하기"
    )
){
    var internalValue by remember { mutableStateOf("") }
    val displayValue = value ?: internalValue
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EveryLoLTheme.color.grayScale1000)
                    .border(
                        1.dp,
                        if (expanded) EveryLoLTheme.color.grayScale200 else EveryLoLTheme.color.grayScale800,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayValue.ifEmpty { "탈퇴 사유를 알려주세요." },
                    style = EveryLoLTheme.typography.subtitle03,
                    color = if (displayValue.isEmpty()) EveryLoLTheme.color.gray800 else EveryLoLTheme.color.grayScale200
                )
                Icon(
                    painter = if(expanded) painterResource(Res.drawable.ic_arrow_up) else painterResource(Res.drawable.ic_arrow_bottom),
                    contentDescription = "Arrow Bottom",
                    tint = EveryLoLTheme.color.grayScale600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EveryLoLTheme.color.grayScale1000)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(12.dp, 20.dp)
            ) {
                reasons.forEachIndexed { index, reason ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(
                                text = reason,
                                style = EveryLoLTheme.typography.subtitle03,
                                color = EveryLoLTheme.color.white200
                            )
                        },
                        onClick = {
                            internalValue = reason
                            onValueChange(reason)
                            expanded = false
                        }
                    )
                    if (index < reasons.lastIndex) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}