package every.lol.com.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme

@Composable
fun DefaultScreen(
    text: String?=null,
    title: String?=null,
    description: String?=null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        text?.let{
            Text(
                text = "${it}화면 준비 중입니다",
                style = EveryLoLTheme.typography.body01
            )
        }
        title?.let{
            Text(
                text = it,
                style = EveryLoLTheme.typography.subtitle02,
                color = EveryLoLTheme.color.white200
            )
        }
        description?.let {
            Text(
                text = it,
                style = EveryLoLTheme.typography.pretendardBody02,
                color = EveryLoLTheme.color.community600
            )
        }
    }
}