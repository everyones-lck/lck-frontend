package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PatchMyTeamRequest
import every.lol.com.core.network.model.request.PatchProfileRequest
import every.lol.com.core.network.model.response.CommentsResponse
import every.lol.com.core.network.model.response.PatchProfileResponse
import every.lol.com.core.network.model.response.PostsResponse
import every.lol.com.core.network.model.response.ProfileResponse
import every.lol.com.core.network.model.response.mypage.GetPredictionsRespponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod.Companion.Patch
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
                if (request.profileImage != null) {
                    append("profileImage", request.profileImage, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"profile.jpg\"")
                    })
                }
                else {
                    append("profileImage", byteArrayOf(), Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(HttpHeaders.ContentDisposition, "filename=\"\"")
                    })
                }
                append("updateProfileRequest",  Json.encodeToString(request.request), Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                })
            }
        ) {
            method = Patch
        }
    }.asApiResponse()

    override suspend fun patchMyTeam(request: PatchMyTeamRequest): ApiResponse<Unit?> = runCatching {
        httpClient.patch("/my-pages/my-team") {
            setBody(request)
        }
    }.asApiResponse()


    override suspend fun getPosts(page: Int, size: Int): ApiResponse<PostsResponse> = runCatching {
        httpClient.get("/my-pages/posts"){
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun getComments(page: Int, size: Int): ApiResponse<CommentsResponse> = runCatching {
        httpClient.get("/my-pages/comments"){
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun withdrawal(): ApiResponse<Unit?> = runCatching {
        httpClient.delete("/my-pages/withdrawal")
    }.asApiResponse()

    override suspend fun logout(refreshToken: String): ApiResponse<Unit?> = runCatching {
        httpClient.delete("/my-pages/logout") {
            headers {
                append("Refresh", refreshToken)
            }
        }
    }.asApiResponse()

    override suspend fun getPredictions(): ApiResponse<GetPredictionsRespponse> = runCatching {
        httpClient.get("/my-pages/votes/predictions")
    }.asApiResponse()
}