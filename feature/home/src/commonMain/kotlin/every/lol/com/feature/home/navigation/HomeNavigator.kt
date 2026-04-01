package every.lol.com.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.home.HomeRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(route = Route.Home, navOptions = navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onNavigateToMypage: () -> Unit,
) {
    composable<Route.Home> {
        HomeRoute(
            onNavigateToMypage = onNavigateToMypage
        )
    }
}