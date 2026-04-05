package every.lol.com.core.model.aboutlck.team

data class AboutLCKTeamPlayerHistory(
    val seasonDetails: List<SeasonDetails>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

//team/player-history
data class SeasonDetails(
    val players: List<Player>,
    val numberOfPlayerDetail: Int,
    val seasonName: String
)
data class Player(
    val playerId: Int,
    val playerName: String,
    val PlayerRole: String,
    val playerPosition: String
)
