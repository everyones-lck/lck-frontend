package every.lol.com.core.model.mypage


data class MypagePom(
    val mvpVoteDetails: List<MypagePomDetail>
)

data class MypagePomDetail(
    val matchId: Int,
    val playerId: Int,
    val playerName: String,
    val position: String,
    val voteDate: String
)