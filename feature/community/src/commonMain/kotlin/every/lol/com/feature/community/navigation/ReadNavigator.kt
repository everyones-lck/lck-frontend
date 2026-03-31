package every.lol.com.feature.community.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import every.lol.com.core.navigation.Route
import every.lol.com.feature.community.ReadRoute


fun NavController.navigateRead(postId: Int, navOptions: NavOptions? = null) {
    navigate(route = Route.Read, navOptions = navOptions)
}

fun NavGraphBuilder.readNavGraph(
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    composable<Route.Read> { backStackEntry ->
        val readRoute: Route.Read = backStackEntry.toRoute()
        ReadRoute(
            postId = readRoute.postId,
            innerPadding = innerPadding,
            onBackClick = onBackClick
        )
    }
}