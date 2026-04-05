package every.lol.com.core.network.model.response.aboutlck.player

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKPlayerTeamHistoryResponse(
    val seasonTeamDetails: List<SeasonTeamDetailsResponse>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

@Serializable
data class SeasonTeamDetailsResponse(
    val teamName: String,
    val seasonName: String
)