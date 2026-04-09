package every.lol.com.core.network.model.response.mypage

import kotlinx.serialization.Serializable


@Serializable
data class GetPredictionsRespponse(
    val correctCount: Int,
    val topPercent: Double,
    val predictionDetails: List<GetPredictionDetailResponse>
)

@Serializable
data class GetPredictionDetailResponse(
    val matchId: Int,
    val matchDate: String,
    val team1Name: String,
    val team2Name: String,
    val predictedTeamName: String,
    val isPredictionSuccessful: Boolean
)