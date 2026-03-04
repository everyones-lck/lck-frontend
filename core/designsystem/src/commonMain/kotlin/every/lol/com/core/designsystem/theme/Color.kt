package every.lol.com.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

interface EveryLoLColor{
    val grayScale100: Color
    val grayScale200: Color
    val grayScale300: Color
    val grayScale400: Color
    val grayScale500: Color
    val grayScale600: Color
    val grayScale700: Color
    val grayScale800: Color
    val grayScale900: Color
    val grayScale1000: Color
}

@Stable
object EveryLoLDarkColor :EveryLoLColor{
    override val grayScale100 = Color(0xFFF7F7F7)
    override val grayScale200 = Color(0xFFF0F0F0)
    override val grayScale300 = Color(0xFFE8E8E8)
    override val grayScale400 = Color(0xFFE1E1E1)
    override val grayScale500 = Color(0xFFD9D9D9)
    override val grayScale600 = Color(0xFFAEAEAE)
    override val grayScale700 = Color(0xFF828282)
    override val grayScale800 = Color(0xFF575757)
    override val grayScale900 = Color(0xFF2B2B2B)
    override val grayScale1000 = Color(0xFF0C0C0C)
}