package every.lol.com.core.network.model.response.mypage

import kotlinx.serialization.Serializable


@Serializable
data class GetPogResponse(
    val setPogVoteDetails: List<GetSetPogVoteDetailResponse>
)

@Serializable
data class GetSetPogVoteDetailResponse(
    val matchId: Int,
    val setIndex: Int,
    val playerId: Int,
    val playerName: String,
    val position: String,
    val voteDate: String
)