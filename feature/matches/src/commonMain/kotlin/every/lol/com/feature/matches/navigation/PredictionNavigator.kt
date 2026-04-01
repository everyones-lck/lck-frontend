package every.lol.com.feature.matches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import every.lol.com.core.navigation.Route
import every.lol.com.feature.matches.PredictionRoute

fun NavController.navigatePrediction(navOptions: NavOptions) {
    navigate(route = Route.Prediction, navOptions = navOptions)
}

fun NavGraphBuilder.predictionNavGraph(
    onBackClick: () -> Unit,
    onResultClick: () -> Unit
) {
    composable<Route.Prediction> { backStackEntry->
        val predictionRoute: Route.Prediction = backStackEntry.toRoute()
        PredictionRoute(
            matchId = predictionRoute.matchId,
            onBackClick = onBackClick,
            onResultClick = onResultClick
        )
    }
}