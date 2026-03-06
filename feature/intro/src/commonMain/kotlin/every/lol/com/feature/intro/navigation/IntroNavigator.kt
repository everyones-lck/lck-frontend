package every.lol.com.feature.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import every.lol.com.core.navigation.Route
import every.lol.com.feature.intro.IntroRoute
import every.lol.com.feature.intro.IntroViewModel


fun NavController.navigateIntro(navOptions: NavOptions) {
    navigate(route = Route.Intro, navOptions = navOptions)
}

fun NavGraphBuilder.introNavGraph(
    onNavigateHome: () -> Unit,
    onLoginClick: () -> Unit
) {
    composable<Route.Intro> {
        IntroRoute(
            viewModel = IntroViewModel(),
            onNavigateHome = onNavigateHome
        )
    }
}