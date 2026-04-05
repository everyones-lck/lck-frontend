package every.lol.com.feature.matches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import every.lol.com.core.navigation.Route
import every.lol.com.feature.matches.LiveResultRoute

fun NavController.navigateLiveResult(matchId: Long, navOptions: NavOptions? = null) {
    navigate(route = Route.LiveResult(matchId), navOptions = navOptions)
}

fun NavGraphBuilder.liveResultNavGraph(
    onBackClick: () -> Unit
) {
    composable<Route.LiveResult> { backStackEntry ->
        val liveResultRoute: Route.LiveResult = backStackEntry.toRoute()

        LiveResultRoute(
            matchId = liveResultRoute.matchId,
            onBackClick = onBackClick
        )
    }
}