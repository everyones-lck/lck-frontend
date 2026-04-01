package every.lol.com.core.model

data class MatchCardModel(
    val matchId: Long,
    val matchDate: String,
    val matchStatus: MatchStatus,
    val seasonName: String,
    val groupName: String?,
    val roundName: String,
    val team1Id: Int,
    val team1Name: String,
    val team2Id: Int,
    val team2Name: String,
    val team1VoteRate: Double = 0.0,
    val team2VoteRate: Double = 0.0,
    val totalVoteCount: Int = 0,
    val predictedWinnerTeamName: String? = null
)

data class MatchInfo(
    val matchInfo: List<Matches>
)

data class Matches(
    val matchId: Long,
    val matchDate: String,
    val matchStatus: MatchStatus,
    val seasonName: String,
    val groupName: String?,
    val roundName: String,
    val team1: MatchesTeam,
    val team2: MatchesTeam
)

data class MatchesTeam(
    val teamId: Int,
    val teamName: String,
    val winner: Boolean
)

data class MatchVoteRate(
    val matchId: Long,
    val team1: MatchVoteTeam,
    val team2: MatchVoteTeam,
    val totalVoteCount: Int
)

data class MatchVoteTeam(
    val teamId: Int,
    val voteCount: Int,
    val voteRate: Double
)