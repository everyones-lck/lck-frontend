package every.lol.com.feature.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.component.EverylolButton
import every.lol.com.core.designsystem.component.EverylolModal
import every.lol.com.core.designsystem.component.EverylolTopAppBar
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.ui.ext.everylolDefault
import every.lol.com.feature.mypage.component.WithdrawalReasonSection
import every.lol.com.feature.mypage.model.MypageUiState
import everylol.feature.mypage.generated.resources.Res
import everylol.feature.mypage.generated.resources.img_withdrawal
import org.jetbrains.compose.resources.painterResource

@Composable
fun MypageWithdrawalScreen(
    state: MypageUiState,
    onBackClick: () -> Unit,
    onWithdrawalClick:() -> Unit ={},
    onWithdrawalConfrimClick:() -> Unit = {},
    onWithdrawalDismissClick:() -> Unit = {},
){
    val snackbarHostState = remember { SnackbarHostState() }
    var showWithdrawalModal by remember { mutableStateOf(false) }
    var isSelectedReason by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .everylolDefault(EveryLoLTheme.color.newBg),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                EverylolTopAppBar(onBackClick = onBackClick, title = "계정탈퇴")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(top=44.dp, bottom = 28.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ){
                    Image(
                        painter = painterResource(Res.drawable.img_withdrawal),
                        contentDescription = null,
                        modifier = Modifier.size(75.dp).clip(CircleShape)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Text(
                            text = "탈퇴 전, 잠시만요",
                            style = EveryLoLTheme.typography.title02,
                            color = EveryLoLTheme.color.grayScale200
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            Text(text = "지금까지 Every.LOL을 사용해주셔서 감사합니다", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.community600)
                            Text(text = "혹시, 불편하셨던 점이 있었다면 들려주세요", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.community600)
                            Text(text = "더 나은 Every.LOL을 만드는데 큰 도움이 됩니다", style = EveryLoLTheme.typography.pretendardBody02, color = EveryLoLTheme.color.community600)
                        }
                    }
                }
                WithdrawalReasonSection(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), onValueChange = {
                    isSelectedReason = it.isNotEmpty()
                })
            }

        }
        EverylolButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxWidth(),
            text = "확인",
            enabled = isSelectedReason,
            onClick = { showWithdrawalModal = true }
        )
        if(showWithdrawalModal) {
            EverylolModal(
                title = "탈퇴 전, 이건 꼭 확인해주세요",
                context = "회원 정보는 삭제되지만,\n게시물·댓글은 삭제되지 않습니다.\n탈퇴 후엔 본인 확인 및 삭제가 불가하므로\n필요 시, 탈퇴 전에 미리 정리해 주세요.",
                confirmText = "확인",
                onConfirm = onWithdrawalConfrimClick,
                dismissText = "취소",
                onDismiss = { showWithdrawalModal = false }
            )
        }
    }
}