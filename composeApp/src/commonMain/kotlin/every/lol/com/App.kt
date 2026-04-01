package every.lol.com

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import every.lol.com.feature.community.navigation.communityNavGraph
import every.lol.com.feature.community.navigation.readNavGraph
import every.lol.com.feature.community.navigation.writeNavGraph
import every.lol.com.feature.home.navigation.homeNavGraph
import every.lol.com.feature.intro.navigation.introNavGraph
import every.lol.com.feature.matches.navigation.matchesNavGraph
import every.lol.com.feature.matches.navigation.predictionNavGraph
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
                containerColor = EveryLoLTheme.color.newBg,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    val isIntro = currentDestination?.hasRoute<Route.Intro>() == true
                    val isMypage = currentDestination?.hasRoute<Route.Mypage>() == true
                    val isWrite = currentDestination?.hasRoute<Route.Write>() == true
                    val isRead = currentDestination?.hasRoute<Route.Read>() == true
                    val isPrediction = currentDestination?.hasRoute<Route.Prediction>()==true

                    if (!isIntro && !isMypage && !isWrite && !isRead && !isPrediction) {
                        Surface(
                            color = EveryLoLTheme.color.newBg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = strokeWidth
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 12.dp)
                                    .selectableGroup(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MainTab.entries.forEach { tab ->
                                    val isSelected =
                                        currentDestination?.hasRoute(tab.route::class) == true

                                    NavigationBarItem(
                                        selected = isSelected,
                                        label = { Text(tab.label, style = EveryLoLTheme.typography.label03) },
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    if (isSelected) tab.selectedIcon else tab.unselectedIcon
                                                ),
                                                modifier = Modifier.size(32.dp),
                                                contentDescription = tab.label
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = Color.Transparent,
                                            selectedIconColor = EveryLoLTheme.color.grayScale100,
                                            selectedTextColor = EveryLoLTheme.color.grayScale100,
                                            unselectedIconColor = EveryLoLTheme.color.grayScale700,
                                            unselectedTextColor = EveryLoLTheme.color.grayScale700
                                        ),
                                        onClick = {
                                            navController.navigate(tab.route) {
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
                    homeNavGraph(
                        onNavigateToMypage = {
                            navController.navigate(Route.Mypage)
                        }
                    )
                    matchesNavGraph(
                        onPredictionClick = { matchId ->
                            navController.navigate(Route.Prediction(matchId))
                        }
                    )

                    predictionNavGraph(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onResultClick = {
                            //Todo: 구현
                        }
                    )

                    composable<Route.AboutLCK> { AboutLCKScreen() }

                    communityNavGraph(
                        innerPadding = innerPadding,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onReadClick = { postId ->
                            navController.navigate(Route.Read(postId))
                        },
                        onWriteClick = {
                            navController.navigate(Route.Write)
                        }
                    )

                    readNavGraph(
                        innerPadding = innerPadding,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )

                    writeNavGraph(
                        innerPadding = innerPadding,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )

                    mypageNavGraph(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onLogoutSuccess = {
                            navController.navigate(Route.Intro) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        onWithdrawalSuccess = {
                            navController.navigate(Route.Intro) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        navToCommunityRead = { postId ->
                            navController.navigate(Route.Read(postId))
                        }
                    )
                }
            }
        }
    }
}