package every.lol.com.feature.intro.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.feature.intro.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageBottomSheet(
    onDismissRequest: () -> Unit,
    onGalleryClick: () -> Unit,
    onDefaultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = EveryLoLTheme.color.grayScale1000,
        scrimColor = EveryLoLTheme.color.grayScale900.copy(alpha = 0.8f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        EveryLoLTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .padding(top=48.dp, bottom = 54.dp),
                verticalArrangement = Arrangement.spacedBy(47.dp)
            ) {
                Text(
                    text = "사진 변경",
                    style = EveryLoLTheme.typography.heading01,
                    color = EveryLoLTheme.color.grayScale200,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{
                            onGalleryClick()
                            onDismissRequest()
                    }
                )

                Text(
                    text = "기본 사진 사용",
                    style = EveryLoLTheme.typography.heading01,
                    color = EveryLoLTheme.color.grayScale200,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{
                            onDefaultClick()
                            onDismissRequest()
                    }
                )
            }
        }
    }
}