package every.lol.com.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.unit.dp
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import everylol.core.ui.generated.resources.Res
import everylol.core.ui.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

@Composable
fun NicknameValidation(
    modifier: Modifier = Modifier,
    status: Int?=0, //default, success, fail
    message: String
){
    val contentColor = when (status) {
        1 -> EveryLoLTheme.color.semanticCheck
        2 -> EveryLoLTheme.color.semanticWarning
        else -> EveryLoLTheme.color.grayScale800
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = "Nickname Validation",
            tint = contentColor
        )
        Text(
            text = message,
            style = EveryLoLTheme.typography.subtitle04,
            color = contentColor
        )
    }
}