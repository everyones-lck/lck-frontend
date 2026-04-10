package every.lol.com.core.domain.usecase.mypage

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.mypage.MypagePog

class GetMyPogUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(): Result<MypagePog> =
        myPagesRepository.getPog()
}