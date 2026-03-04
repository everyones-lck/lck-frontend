package every.lol.com.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object Matches : Route

    @Serializable
    data object AboutLCK : Route

    @Serializable
    data object Community : Route

    @Serializable
    data object Intro : Route

}