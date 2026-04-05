package every.lol.com.feature.aboutlck.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.aboutlck.AboutLCKRoute

fun NavController.navigateAboutLCK(navOptions: NavOptions) {
    navigate(route = Route.AboutLCK, navOptions = navOptions)
}

fun NavGraphBuilder.aboutLCKNavGraph(

) {
    composable<Route.AboutLCK> {
        AboutLCKRoute(

        )
    }
}