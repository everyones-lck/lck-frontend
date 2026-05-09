package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.domain.repository.TermsRepository
import every.lol.com.core.model.TermsDetail
import every.lol.com.core.network.datasource.TermsDataSource

class TermsRepositoryImpl(
    private val remote: TermsDataSource
): TermsRepository {

    override suspend fun getTermsDetail(termId: Int): Result<TermsDetail> =
        remote.getTermsDetail(termId).toResult().map {
            TermsDetail(it.id, it.title, it.content, it.isRequired)
        }

}