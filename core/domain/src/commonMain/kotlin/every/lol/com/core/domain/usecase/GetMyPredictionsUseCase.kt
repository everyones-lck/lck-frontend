package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.mypage.MypagePredictions


class GetMyPredictionsUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(): Result<MypagePredictions> =
        myPagesRepository.getPredictions()
}