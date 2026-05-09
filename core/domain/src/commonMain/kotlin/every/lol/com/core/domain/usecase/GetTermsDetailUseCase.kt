package every.lol.com.core.domain.usecase

import every.lol.com.core.domain.repository.TermsRepository
import every.lol.com.core.model.TermsDetail


class GetTermsDetailUseCase(
    private val termsRepository: TermsRepository
) {
    suspend operator fun invoke(termId: Int): Result<TermsDetail> {
        return termsRepository.getTermsDetail(termId)
    }
}