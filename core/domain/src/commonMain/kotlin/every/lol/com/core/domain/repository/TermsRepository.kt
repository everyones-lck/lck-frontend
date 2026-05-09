package every.lol.com.core.domain.repository

import every.lol.com.core.model.TermsDetail

interface TermsRepository {
    suspend fun getTermsDetail(termId: Int): Result<TermsDetail>
}