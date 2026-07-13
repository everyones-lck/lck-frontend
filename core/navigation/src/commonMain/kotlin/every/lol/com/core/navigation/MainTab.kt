package every.lol.com.core.navigation

import everylol.core.navigation.generated.resources.Res
import everylol.core.navigation.generated.resources.ic_aboutlck_active
import everylol.core.navigation.generated.resources.ic_aboutlck_none
import everylol.core.navigation.generated.resources.ic_community_active
import everylol.core.navigation.generated.resources.ic_community_none
import everylol.core.navigation.generated.resources.ic_home_active
import everylol.core.navigation.generated.resources.ic_home_none
import everylol.core.navigation.generated.resources.ic_matches_active
import everylol.core.navigation.generated.resources.ic_matches_none
import org.jetbrains.compose.resources.DrawableResource

enum class MainTab (
    val route: Route,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
){
    Home(Route.Home, "홈", Res.drawable.ic_home_active, Res.drawable.ic_home_none),
    Matches(Route.Matches, "오늘의 매치",Res.drawable.ic_matches_active, Res.drawable.ic_matches_none),
    AboutLCK(Route.AboutLCK, "LCK 대해", Res.drawable.ic_aboutlck_active, Res.drawable.ic_aboutlck_none),
    Community(Route.Community, "커뮤니티", Res.drawable.ic_community_active, Res.drawable.ic_community_none)
}