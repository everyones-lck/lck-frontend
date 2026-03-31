package every.lol.com.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
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
    val teamDK: Color
    val teamBFX: Color
    val teamHLE: Color
    val teamGen: Color
    val teamT1KT: Color
    val teamBrion: Color
    val teamDRXDNF: Color
    val teamNS: Brush
    val semanticWarning: Color
    val semanticCheck: Color
    val newBg: Color
    val community600: Color
    val white200: Color
    val gray800 : Color
    val black900 : Color
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
    override val teamDK = Color(0xFFFFFFFF)
    override val teamBFX = Color(0xFFF8E52F)
    override val teamHLE = Color(0xFFF3741B)
    override val teamGen = Color(0xFFAA8B30)
    override val teamT1KT = Color(0xFFE91B3B)
    override val teamBrion = Color(0xFF02694C)
    override val teamDRXDNF = Color(0xFF0017E7)
    override val teamNS = Brush.linearGradient(colors = listOf(Color(0xFF2B2B2B), Color(0xFFE91C20)))
    override val semanticWarning = Color(0xFFF26E6E)
    override val semanticCheck = Color(0xFFC5E3BA)
    override val newBg = Color(0xFF131313)
    override val community600 = Color(0xFFAEAEAE)
    override val white200 = Color(0xFFF0F0F0)
    override val gray800 = Color(0xFF767373)
    override val black900 = Color(0xFF2B2B2B)
}