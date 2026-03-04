package every.lol.com.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import everylol.core.navigation.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

enum class MainTab (
    val route: Route,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
){
    Home(Route.Home, "홈", Res.drawable.ic_home_active, Res.drawable.ic_home_none),
    Matches(Route.Matches, "경기",Res.drawable.ic_matches_active, Res.drawable.ic_matches_none),
    AboutLCK(Route.AboutLCK, "소개", Res.drawable.ic_aboutlck_active, Res.drawable.ic_aboutlck_none),
    Community(Route.Community, "커뮤니티", Res.drawable.ic_community_active, Res.drawable.ic_community_none)
}