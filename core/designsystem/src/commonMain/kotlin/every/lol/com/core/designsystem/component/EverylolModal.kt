package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun EverylolModal (
    title: String? = null,
    context: String? = null,
    confirmText: String? = "확인",
    onConfirm: () -> Unit = {},
    dismissText: String? = "취소",
    onDismiss: () -> Unit = {},
    modifier : Modifier = Modifier
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveryLoLTheme.color.grayScale900.copy(alpha = 0.8f)),
        contentAlignment= Alignment.Center
    ) {
        Column(
            modifier = modifier
                .align(Alignment.Center)
                .padding(horizontal = 28.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(EveryLoLTheme.color.grayScale100)
                .padding(24.dp, 24.dp, 24.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title?.let {
                Text(
                    text = title,
                    style = EveryLoLTheme.typography.subtitle02,
                    color = EveryLoLTheme.color.grayScale1000
                )
            }
            context?.let {
                Text(
                    text = context,
                    style = EveryLoLTheme.typography.pretendardBody02,
                    color = EveryLoLTheme.color.community600
                )
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
                                onClick = onConfirm
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