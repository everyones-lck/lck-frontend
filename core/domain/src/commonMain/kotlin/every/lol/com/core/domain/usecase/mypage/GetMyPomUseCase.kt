package every.lol.com.core.domain.usecase.mypage

import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.mypage.MypagePog
import every.lol.com.core.model.mypage.MypagePom

class GetMyPomUseCase(
    private val myPagesRepository: MyPagesRepository
) {
    suspend operator fun invoke(): Result<MypagePom> =
        myPagesRepository.getPom()
}