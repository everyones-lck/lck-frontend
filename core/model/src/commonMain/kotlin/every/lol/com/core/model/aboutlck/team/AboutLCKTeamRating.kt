package every.lol.com.core.model.aboutlck.team

data class AboutLCKTeamRating(
    val teamDetailList: List<TeamDetailList>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

//team/rating
data class TeamDetailList(
    val teamId: Int,
    val teamName: String,
    val teamLogoUrl: String,
    val rating: Int
)
