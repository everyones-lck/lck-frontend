package every.lol.com.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import everylol.core.designsystem.generated.resources.Res
import everylol.core.designsystem.generated.resources.esamanru_bold
import everylol.core.designsystem.generated.resources.esamanru_light
import everylol.core.designsystem.generated.resources.esamanru_medium
import everylol.core.designsystem.generated.resources.pretendard_extrabold
import everylol.core.designsystem.generated.resources.pretendard_medium
import org.jetbrains.compose.resources.Font

val Esamanru: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.esamanru_light, FontWeight.W300),
        Font(Res.font.esamanru_medium, FontWeight.W500),
        Font(Res.font.esamanru_bold, FontWeight.W600)
    )

val Pretendard: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.pretendard_medium, FontWeight.W500),
        Font(Res.font.pretendard_extrabold, FontWeight.W600)
    )

@Immutable
data class EveryLoLTypography(
    val playname01: TextStyle,
    val title01: TextStyle,
    val title02: TextStyle,
    val heading01: TextStyle,
    val heading02: TextStyle,
    val body01: TextStyle,
    val body02: TextStyle,
    val body03: TextStyle,
    val label01: TextStyle,
    val label02: TextStyle,
    val label03: TextStyle,
    val subtitle02: TextStyle,
    val subtitle03: TextStyle,
    val subtitle04: TextStyle
)

val everyLoLTypography: EveryLoLTypography
    @Composable
    get() {
        val esamanru = Esamanru
        val pretendard = Pretendard
        return EveryLoLTypography(
            playname01 = TextStyle(fontFamily = esamanru, fontSize = 42.sp, fontWeight = FontWeight.W600),
            title01 = TextStyle(fontFamily = esamanru, fontSize = 20.sp, fontWeight = FontWeight.W500),
            title02 = TextStyle(fontFamily = esamanru, fontSize = 24.sp, fontWeight = FontWeight.W300),
            heading01 = TextStyle(fontFamily = esamanru, fontSize = 20.sp, fontWeight = FontWeight.W500),
            heading02 = TextStyle(fontFamily = esamanru, fontSize = 20.sp, fontWeight = FontWeight.W300),
            body01 = TextStyle(fontFamily = esamanru, fontSize = 18.sp, fontWeight = FontWeight.W300),
            body02 = TextStyle(fontFamily = esamanru, fontSize = 16.sp, fontWeight = FontWeight.W500),
            body03 = TextStyle(fontFamily = esamanru, fontSize = 16.sp, fontWeight = FontWeight.W300),
            label01 = TextStyle(fontFamily = esamanru, fontSize = 14.sp, fontWeight = FontWeight.W500),
            label02 = TextStyle(fontFamily = esamanru, fontSize = 14.sp, fontWeight = FontWeight.W300),
            label03 = TextStyle(fontFamily = esamanru, fontSize = 12.sp, fontWeight = FontWeight.W300),
            subtitle02 = TextStyle(fontFamily = pretendard, fontSize = 16.sp, fontWeight = FontWeight.W600),
            subtitle03 = TextStyle(fontFamily = pretendard, fontSize = 14.sp, fontWeight = FontWeight.W600),
            subtitle04 = TextStyle(fontFamily = pretendard, fontSize = 12.sp, fontWeight = FontWeight.W500)
        )
    }
