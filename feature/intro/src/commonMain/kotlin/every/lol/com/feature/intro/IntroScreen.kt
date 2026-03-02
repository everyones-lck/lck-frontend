package every.lol.com.feature.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun IntroScreen(
    onIntroComplete: () -> Unit // ◀ 이 파라미터를 추가해야 합니다.
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 임시로 버튼을 눌렀을 때 넘어가도록 설정
        Button(onClick = onIntroComplete) {
            Text("로그인/시작하기")
        }
    }
}