package every.lol.com.feature.matches.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import every.lol.com.core.navigation.Route
import every.lol.com.feature.matches.PredictionRoute

fun NavController.navigatePrediction(matchId: Long, navOptions: NavOptions? = null) {
    navigate(route = Route.Prediction(matchId), navOptions = navOptions)
}

fun NavGraphBuilder.predictionNavGraph(
    onBackClick: () -> Unit,
    onResultClick: (Long) -> Unit
) {
    composable<Route.Prediction> { backStackEntry->
        val predictionRoute: Route.Prediction = backStackEntry.toRoute()
        PredictionRoute(
            matchId = predictionRoute.matchId,
            onBackClick = onBackClick,
            onResultClick = {
                onResultClick(predictionRoute.matchId)
            }
        )
    }
}