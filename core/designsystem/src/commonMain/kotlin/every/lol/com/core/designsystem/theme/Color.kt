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
    val teamDK: Color
    val teamBFX: Color
    val teamHLE: Color
    val teamGen: Color
    val teamT1: Color
    val teamKT: Color
    val teamBRO: Color
    val teamKRX: Color
    val teamDNS: Color
    val teamNS: Color
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
    override val teamDK = Color(0xFF92E1E6)
    override val teamBFX = Color(0xFFFBE400)
    override val teamHLE = Color(0xFFF37321)
    override val teamGen = Color(0xFFCFB887)
    override val teamT1 = Color(0xFFE2012D)
    override val teamKT = Color(0xFFFF0A07)
    override val teamDNS = Color(0xFF1102A3)
    override val teamBRO = Color(0xFF02694C)
    override val teamKRX = Color(0xFFE5007F)
    override val teamNS = Color(0xFFE81B3B)
    override val semanticWarning = Color(0xFFF26E6E)
    override val semanticCheck = Color(0xFFC5E3BA)
    override val newBg = Color(0xFF131313)
    override val community600 = Color(0xFFAEAEAE)
    override val white200 = Color(0xFFF0F0F0)
    override val gray800 = Color(0xFF767373)
    override val black900 = Color(0xFF2B2B2B)
}