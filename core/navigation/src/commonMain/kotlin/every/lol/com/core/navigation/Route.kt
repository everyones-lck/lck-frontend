package every.lol.com.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object Matches : Route

    @Serializable
    data class Prediction(val matchId: Long) : Route

    @Serializable
    data class LiveResult(val matchId: Long) : Route

    @Serializable
    data object AboutLCK : Route

    @Serializable
    data object Community : Route

    @Serializable
    data object Write : Route

    @Serializable
    data class Read(val postId: Int) : Route

    @Serializable
    data object Intro : Route

    @Serializable
    data object Mypage : Route
}