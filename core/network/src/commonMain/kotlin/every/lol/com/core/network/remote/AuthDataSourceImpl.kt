package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.LoginRequest
import every.lol.com.core.network.model.response.TokenResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthDataSourceImpl(
    private val httpClient: HttpClient
): AuthDataSource {

    override suspend fun login(request: LoginRequest): ApiResponse<TokenResponse>
        = httpClient.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.asApiResponse()

}