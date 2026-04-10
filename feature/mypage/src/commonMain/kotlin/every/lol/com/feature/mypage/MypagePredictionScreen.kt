package every.lol.com.feature.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.component.DefaultScreen
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.Predictions
import every.lol.com.feature.mypage.model.MypageUiState
import everylol.feature.mypage.generated.resources.Res
import everylol.feature.mypage.generated.resources.img_top_001
import everylol.feature.mypage.generated.resources.img_top_003
import everylol.feature.mypage.generated.resources.img_top_005
import everylol.feature.mypage.generated.resources.img_top_010
import everylol.feature.mypage.generated.resources.img_top_020
import everylol.feature.mypage.generated.resources.img_top_030
import everylol.feature.mypage.generated.resources.img_top_050
import everylol.feature.mypage.generated.resources.img_top_070
import everylol.feature.mypage.generated.resources.img_top_090
import everylol.feature.mypage.generated.resources.img_top_100
import org.jetbrains.compose.resources.painterResource

@Composable
fun MypagePredictionScreen(
    state: MypageUiState,
    onBackClick: () -> Unit={},
){
    val snackbarHostState = remember { SnackbarHostState() }
    val predictionState = state as? MypageUiState.Prediction
    val rank = predictionState?.rank ?: 0

    val rankImage = when {
        rank <= 1 -> Res.drawable.img_top_001
        rank <= 3 -> Res.drawable.img_top_003
        rank <= 5 -> Res.drawable.img_top_005
        rank <= 10 -> Res.drawable.img_top_010
        rank <= 20 -> Res.drawable.img_top_020
        rank <= 30 -> Res.drawable.img_top_030
        rank <= 50 -> Res.drawable.img_top_050
        rank <= 70 -> Res.drawable.img_top_070
        rank <= 90 -> Res.drawable.img_top_090
        else -> Res.drawable.img_top_100
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "승부예측 투표내역")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(34.dp,24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Image(
                        modifier = Modifier
                            .weight(2f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale1000)
                            .padding(24.dp),
                        painter = painterResource(rankImage),
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EveryLoLTheme.color.grayScale1000)
                            .padding(14.dp, 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 4.dp),
                                text = "TOP",
                                style = EveryLoLTheme.typography.pretendardBody01,
                                color = EveryLoLTheme.color.community600
                            )
                            Text(
                                text = "${rank.toString()}%",
                                style = EveryLoLTheme.typography.playname01,
                                color = EveryLoLTheme.color.white200
                            )
                        }
                        Text(
                            text = "참여자 중 상위 ${rank.toString()}% 달성",
                            style = EveryLoLTheme.typography.pretendardBody02,
                            color = EveryLoLTheme.color.community600
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top=12.dp)
                ) {
                    predictionState?.let { data ->
                        val predictionList = data.data ?: emptyList()

                        if (predictionList.isEmpty()) {
                            DefaultScreen(
                                title = "투표 내역이 아직 없습니다",
                                description = "첫 승부 예측 투표를 해보세요"
                            )
                        } else {
                            predictionList.forEach { prediction ->
                                Predictions(prediction = prediction)
                            }
                        }
                    }
                }
            }
        }
    }
}