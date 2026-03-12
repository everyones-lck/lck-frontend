package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.KakaoRequest
import every.lol.com.core.network.model.request.NicknameRequest
import every.lol.com.core.network.model.request.SignupRequest
import every.lol.com.core.network.model.response.SignupResponse
import every.lol.com.core.network.model.response.TokenResponse

interface AuthDataSource {
    suspend fun login(request: KakaoRequest): ApiResponse<TokenResponse>
    suspend fun signup(request: SignupRequest): ApiResponse<SignupResponse>
    suspend fun refresh(request: KakaoRequest): ApiResponse<TokenResponse>
    suspend fun nickname(request: NicknameRequest): ApiResponse<Boolean?>
}