package every.lol.com.feature.matches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.matches.MatchesRoute

fun NavController.navigateMatches(navOptions: NavOptions) {
    navigate(route = Route.Matches, navOptions = navOptions)
}

fun NavGraphBuilder.matchesNavGraph(

) {
    composable<Route.Matches> {
        MatchesRoute()
    }
}