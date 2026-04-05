package every.lol.com.core.model.aboutlck.team

data class AboutLCKTeamRatingHistory(
    val seasonDetailList: List<SeasonDetailList>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

data class SeasonDetailList(
    val seasonName: String,
    val rating: Int
)