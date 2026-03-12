package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.AuthDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.KakaoRequest
import every.lol.com.core.network.model.request.NicknameRequest
import every.lol.com.core.network.model.request.SignupRequest
import every.lol.com.core.network.model.response.SignupResponse
import every.lol.com.core.network.model.response.TokenResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthDataSourceImpl(
    private val httpClient: HttpClient
): AuthDataSource {

    override suspend fun login(request: KakaoRequest): ApiResponse<TokenResponse> = runCatching {
        httpClient.post("/auth/login") {
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun signup(request: SignupRequest): ApiResponse<SignupResponse> = runCatching {
        httpClient.post("/auth/signup") {
            setBody(
                MultiPartFormDataContent(
                    formData {

                        append(
                            "signupUserData",
                            Json.encodeToString(request.signupUserData),
                            Headers.build {
                                append(HttpHeaders.ContentType, "application/json")
                            }
                        )

                        val imageContent = request.profileImage ?: byteArrayOf()

                        append("profileImage", imageContent, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"profileImage\"; filename=\"profile.jpg\"")
                        })
                    }
                )
            )
        }
    }.asApiResponse()

    override suspend fun refresh(request: KakaoRequest): ApiResponse<TokenResponse> = runCatching {
        httpClient.post("/auth/refresh") {
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun nickname(request: NicknameRequest): ApiResponse<Boolean?> = runCatching {
        httpClient.post("/auth/nickname") {
            setBody(request)
        }
    }.asApiResponse()

}