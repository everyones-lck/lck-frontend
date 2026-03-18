package every.lol.com

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import every.lol.com.core.designsystem.theme.EveryLoLTheme
import every.lol.com.core.navigation.MainTab
import every.lol.com.core.navigation.Route
import every.lol.com.feature.aboutlck.AboutLCKScreen
import every.lol.com.feature.community.CommunityScreen
import every.lol.com.feature.home.HomeScreen
import every.lol.com.feature.intro.navigation.introNavGraph
import every.lol.com.feature.matches.MatchesScreen
import every.lol.com.feature.mypage.navigation.mypageNavGraph
import moe.tlaster.precompose.PreComposeApp
import org.jetbrains.compose.resources.painterResource
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    PreComposeApp {
        val navController = rememberNavController()

    EveryLoLTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                val isIntro = currentDestination?.hasRoute<Route.Intro>() == true
                val isMypage = currentDestination?.hasRoute<Route.Mypage>() == true

                    if (!isIntro && !isMypage) {
                        NavigationBar {
                            MainTab.entries.forEach { tab ->
                                val isSelected =
                                    currentDestination?.hasRoute(tab.route::class) == true

                                NavigationBarItem(
                                    selected = isSelected,
                                    label = { Text(tab.label) },
                                    icon = {
                                        Icon(
                                            painter = painterResource(
                                                if (isSelected) tab.selectedIcon else tab.unselectedIcon
                                            ),
                                            contentDescription = tab.label
                                        )
                                    },
                                    onClick = {
                                        navController.navigate(tab.route) {
                                            // 탭 전환 시 뒤로가기 스택 관리
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.Intro,
                    modifier = Modifier.fillMaxSize()
                ) {
                    introNavGraph(
                        onNavigateHome = {
                            navController.navigate(Route.Home) {
                                popUpTo<Route.Intro> { inclusive = true }
                            }
                        }
                    )
                    composable<Route.Home> { HomeScreen(innerPadding = innerPadding,
                                                       onNavigateToMypage = {
                        navController.navigate(Route.Mypage)) }
                                                        composable<Route.Matches> { MatchesScreen() }
                    composable<Route.AboutLCK> { AboutLCKScreen() }
                    composable<Route.Community> { CommunityScreen() }

                    mypageNavGraph(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}