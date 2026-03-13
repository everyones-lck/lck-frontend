package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PatchMyTeamRequest
import every.lol.com.core.network.model.request.PatchProfileRequest
import every.lol.com.core.network.model.response.PatchProfileResponse
import every.lol.com.core.network.model.response.ProfileResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MyPagesDataSourceImpl(
    private val httpClient: HttpClient
): MyPagesDataSource {

    override suspend fun getProfile(): ApiResponse<ProfileResponse> =runCatching {
        httpClient.get("/my-pages/profiles")
    }.asApiResponse()

    override suspend fun patchProfile(request: PatchProfileRequest): ApiResponse<PatchProfileResponse> =runCatching {
        httpClient.submitFormWithBinaryData(
            url = "/my-pages/profiles",
            formData = formData {
                request.profileImage?.let { imageBytes ->
                    append("profileImage", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"profile.png\"")
                    })
                }
                val jsonRequest = Json.encodeToString(request.request)
                append("request", jsonRequest, Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                })
            }
        ) {
            method = Patch
        }
    }.asApiResponse()

    override suspend fun patchMyTeam(request: PatchMyTeamRequest): ApiResponse<Unit?> = runCatching {
        httpClient.patch("/my-pages/my-team") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }.asApiResponse()
}