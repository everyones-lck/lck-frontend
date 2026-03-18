package every.lol.com.core.model

data class LckStandingTeamModel(
    val teamId: Long,
    val rank: Int,
    val teamName: String,
    val rightText: String = "-"
)