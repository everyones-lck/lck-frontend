package every.lol.com.core.model.aboutlck.team

data class AboutLCKTeamPlayerInformation(
    val playerDetails: List<PlayerDetail>,
    val numberOfPlayerDetail: Int
)

//team/player-Information
data class PlayerDetail(
    val playerId: Int,
    val playerName: String,
    val playerRole: String
)