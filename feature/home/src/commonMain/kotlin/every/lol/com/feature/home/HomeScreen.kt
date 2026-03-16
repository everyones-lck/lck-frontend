package every.lol.com.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import every.lol.com.core.designsystem.theme.EveryLoLTheme


@Composable
fun HomeScreen(
    onNavigateToMypage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "홈화면 준비 중입니다",
            style = EveryLoLTheme.typography.body01
        )
        Button(onClick = onNavigateToMypage) {
            Text(text = "마이페이지 테스트 가기")
        }
    }
}