package every.lol.com.feature.community.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.community.CommunityRoute


fun NavController.navigateCommunity(navOptions: NavOptions) {
    navigate(route = Route.Community, navOptions = navOptions)
}

fun NavGraphBuilder.communityNavGraph(
    innerPadding: PaddingValues,
    onBackClick: () -> Unit,
    onWriteSuccess: () -> Unit,
    onDeleteSuccess: () -> Unit
) {
    composable<Route.Community> {
        CommunityRoute(
            innerPadding = innerPadding,
            onBackClick = onBackClick,
            onWriteSuccess = onWriteSuccess,
            onDeleteSuccess = onDeleteSuccess
        )
    }
}