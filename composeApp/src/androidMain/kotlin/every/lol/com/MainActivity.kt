package every.lol.com

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.kakao.sdk.common.KakaoSdk
import every.lol.com.core.common.AppContext
import every.lol.com.di.androidModule
import every.lol.com.di.androidNetworkModule
import every.lol.com.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bgColor = 0xFF131313.toInt()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(bgColor),
            navigationBarStyle = SystemBarStyle.dark(bgColor)
        )

        super.onCreate(savedInstanceState)
        AppContext.set(this)
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        if (GlobalContext.getOrNull() == null) {
            initKoin (
                appDeclaration = {
                    androidContext(applicationContext)
                },
                platformModules = listOf(
                    androidNetworkModule,
                    androidModule
                )
            )
        }
        window.setBackgroundDrawable(ColorDrawable(bgColor))

        setContent {
            val context = LocalContext.current

            App(
                onOpenSourceLicenseClick = {
                    val intent = Intent(context, OssLicensesMenuActivity::class.java)
                    context.startActivity(intent)
                    OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                }
            )
        }
    }
}