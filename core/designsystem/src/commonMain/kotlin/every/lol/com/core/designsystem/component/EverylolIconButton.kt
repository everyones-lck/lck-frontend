package every.lol.com.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun EverylolIconButton(
    icon: DrawableResource,
    color: Color = Color.Unspecified,
    size: Int = 24,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(size.dp)
            .background(Color.Transparent),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "icon",
            modifier = Modifier.size(size.dp),
            tint = color
        )
    }
}