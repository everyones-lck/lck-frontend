package every.lol.com.core.model.mypage

data class MypagePredictions(
    val correctCount: Int,
    val topPercent: Double,
    val predictionDetails: List<MypagePredictionDetail>
)

data class MypagePredictionDetail(
    val matchId: Int,
    val matchDate: String,
    val team1Name: String,
    val team2Name: String,
    val predictedTeamName: String,
    val isPredictionSuccessful: Boolean
)