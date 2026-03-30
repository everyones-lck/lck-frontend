package every.lol.com.feature.community.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.community.WriteRoute


fun NavController.navigateWrite(navOptions: NavOptions? = null) {
    navigate(route = Route.Write, navOptions = navOptions)
}

fun NavGraphBuilder.writeNavGraph(
    innerPadding: PaddingValues,
    onBackClick: () -> Unit,
) {
    composable<Route.Write> {
        WriteRoute(
            innerPadding = innerPadding,
            onBackClick = onBackClick,
        )
    }
}