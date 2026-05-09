package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.TermsDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.response.TermsDetailResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class TermsDataSourceImpl(
    private val httpClient: HttpClient
): TermsDataSource {

    override suspend fun getTermsDetail(termId: Int): ApiResponse<TermsDetailResponse> = runCatching {
        httpClient.get("/terms/$termId")
    }.asApiResponse()

}