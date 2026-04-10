package every.lol.com.core.network.model.response.mypage

import kotlinx.serialization.Serializable


@Serializable
data class GetPomResponse(
    val mvpVoteDetails: List<GetMvpVoteDetailResponse>
)

@Serializable
data class GetMvpVoteDetailResponse(
    val matchId: Int,
    val playerId: Int,
    val playerName: String,
    val position: String,
    val voteDate: String
)