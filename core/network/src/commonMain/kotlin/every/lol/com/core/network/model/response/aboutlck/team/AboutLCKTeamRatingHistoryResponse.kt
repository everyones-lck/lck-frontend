package every.lol.com.core.network.model.response.aboutlck.team

import kotlinx.serialization.Serializable


@Serializable
data class AboutLCKTeamRatingHistoryResponse(
    val seasonDetailList: List<SeasonDetailResponse>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

@Serializable
data class SeasonDetailResponse(
    val seasonName: String,
    val rating: Int
)