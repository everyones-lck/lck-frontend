package every.lol.com.core.model.aboutlck.player

data class AboutLCKPlayerTeamHistory(
    val seasonTeamDetails: List<SeasonTeamDetails>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

//player/team-history
data class SeasonTeamDetails(
    val teamName: String,
    val seasonName: String
)