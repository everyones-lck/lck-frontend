package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.LoginRequest
import every.lol.com.core.network.model.response.TokenResponse

interface AuthDataSource {
    suspend fun login(request: LoginRequest): ApiResponse<TokenResponse>
}