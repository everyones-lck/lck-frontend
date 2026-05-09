package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.TermsDetailResponse

interface TermsDataSource {
    suspend fun getTermsDetail(termId: Int): ApiResponse<TermsDetailResponse>
}