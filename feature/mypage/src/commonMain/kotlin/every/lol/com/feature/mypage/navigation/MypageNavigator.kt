package every.lol.com.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.mypage.MypageRoute


fun NavController.navigateMypage(navOptions: NavOptions) {
    navigate(route = Route.Mypage, navOptions = navOptions)
}

fun NavGraphBuilder.mypageNavGraph(
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onWithdrawalSuccess: () -> Unit
) {
    composable<Route.Mypage> {
        MypageRoute(
            onBackClick = onBackClick,
            onLogoutSuccess = onLogoutSuccess,
            onWithdrawalSuccess = onWithdrawalSuccess
        )
    }
}