package every.lol.com.core.model.mypage


data class MypagePog(
    val setPogVoteDetails: List<MypagePogDetail>
)

data class MypagePogDetail(
    val matchId: Int,
    val setIndex: Int,
    val playerId: Int,
    val playerName: String,
    val position: String,
    val voteDate: String
)