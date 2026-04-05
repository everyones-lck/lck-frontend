package every.lol.com.core.network.model.response.aboutlck.team

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamRatingResponse(
    val teamDetailList: List<TeamDetailResponse>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

@Serializable
data class TeamDetailResponse(
    val teamId: Int,
    val teamName: String,
    val teamLogoUrl: String,
    val rating: Int
)