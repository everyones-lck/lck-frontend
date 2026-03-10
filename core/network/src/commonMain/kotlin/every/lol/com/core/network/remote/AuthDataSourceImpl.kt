package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.LoginRequest
import every.lol.com.core.network.model.response.TokenResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthDataSourceImpl(
    private val httpClient: HttpClient
): AuthDataSource {

    override suspend fun login(request: LoginRequest): ApiResponse<TokenResponse>
    = runCatching {
        httpClient.post("/auth/login") {
            setBody(request)
        }
    }.asApiResponse()

}