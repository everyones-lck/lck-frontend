package every.lol.com.feature.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.DefaultScreen
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.MVPItem
import every.lol.com.feature.mypage.model.MypageIntent
import every.lol.com.feature.mypage.model.MypageUiState

@Composable
fun MypageMVPScreen(
    state: MypageUiState,
    onBackClick: () -> Unit={},
    onIntent: (MypageIntent) -> Unit
){
    val snackbarHostState = remember { SnackbarHostState() }
    val mvpState = state as? MypageUiState.MVP
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "MVP 투표내역")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp, 12.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                mvpState?.let{
                    if(it.mvpList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            DefaultScreen(
                                title = "MVP 투표 내역이 아직 없습니다",
                                description = "첫 MVP 투표를 해보세요"
                            )
                        }
                    }
                    else{
                        it.mvpList.forEach { mvp ->
                            MVPItem(mvp = mvp)
                        }
                    }
                }
            }
        }
    }
}