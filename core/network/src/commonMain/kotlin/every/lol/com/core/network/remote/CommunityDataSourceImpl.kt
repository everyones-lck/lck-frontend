package every.lol.com.core.network.remote

import every.lol.com.core.common.openFileStream
import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PostCommentRequest
import every.lol.com.core.network.model.request.PostPostDetailRequest
import every.lol.com.core.network.model.request.PostPostRequest
import every.lol.com.core.network.model.request.ReportCommentRequest
import every.lol.com.core.network.model.request.ReportPostRequest
import every.lol.com.core.network.model.response.PopularPostListResponse
import every.lol.com.core.network.model.response.PostDetailResponse
import every.lol.com.core.network.model.response.PostIdResponse
import every.lol.com.core.network.model.response.PostLikeResponse
import every.lol.com.core.network.model.response.PostListResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.delete
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.writePacket
import io.ktor.utils.io.writer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class CommunityDataSourceImpl(
    private val platformContext: Any,
    private val httpClient: HttpClient
): CommunityDataSource {

    override suspend fun postPost(request: PostPostRequest): ApiResponse<PostIdResponse> =
        withContext(NonCancellable + Dispatchers.IO) {
            runCatching {
                val jsonString = Json.encodeToString(request.request)

                val response = httpClient.post("/post/create") {
                    timeout {
                        requestTimeoutMillis = 900_000L
                        connectTimeoutMillis = 60_000L
                        socketTimeoutMillis = 900_000L
                    }

                    onUpload { bytesSentTotal, contentLength ->
                        if (contentLength != null && contentLength > 0) {
                            val progress = (bytesSentTotal.toDouble() / contentLength * 100).toInt()
                            if (progress % 10 == 0) {
                                println("UPLOAD_DEBUG: Progress = $progress% ($bytesSentTotal / $contentLength)")
                            }
                        }
                    }

                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append("request", jsonString, Headers.build {
                                    append(HttpHeaders.ContentType, "application/json")
                                })

                                request.files?.forEachIndexed { index, mediaFile ->
                                    val contentType = if (mediaFile.isVideo) "video/mp4" else "image/jpeg"
                                    val extension = if (mediaFile.isVideo) "mp4" else "jpg"
                                    val fileName = "file_$index.$extension"

                                    append(
                                        key = "files",
                                        value = ChannelProvider(size = mediaFile.fileSize) {
                                            writer(Dispatchers.IO) {
                                                val input: Input = openFileStream(platformContext, mediaFile.uriString)

                                                try {
                                                    channel.writePacket(input)
                                                } catch (e: Exception) {
                                                    println("UPLOAD_DEBUG: Streaming Error = ${e.message}")
                                                    throw e
                                                } finally {

                                                }
                                            }.channel
                                        },
                                        headers = Headers.build {
                                            append(HttpHeaders.ContentType, contentType)
                                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                                        }
                                    )
                                }
                            }
                        )
                    )
                }

                // 4. 로깅 시 bodyAsText()는 대용량 응답일 경우 메모리를 먹으므로 상태코드 먼저 확인
                println("UPLOAD_DEBUG: Status = ${response.status}")
                if (!response.status.isSuccess()) {
                    val errorBody = response.bodyAsText()
                    println("UPLOAD_DEBUG: Error Body = $errorBody")
                }

                response
            }
        }.asApiResponse()

    override suspend fun editPost(postId: Int, request: PostPostDetailRequest): ApiResponse<PostIdResponse> = runCatching {
        httpClient.patch("/post/$postId/modify"){
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun detailPost(postId: Int): ApiResponse<PostDetailResponse> = runCatching {
        httpClient.get("/post/$postId/detail")
    }.asApiResponse()

    override suspend fun postList(postType: String, page: Int, size: Int): ApiResponse<PostListResponse> = runCatching {
        httpClient.get("/post/list"){
            url {
                parameters.append("postType", postType)
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
    }.asApiResponse()

    override suspend fun popularPostList(period: String): ApiResponse<PopularPostListResponse> = runCatching {
        httpClient.get("/post/popular"){
            url {
                parameters.append("period", period)
            }
        }
    }.asApiResponse()

    override suspend fun deletePost(postId: Int): ApiResponse<Unit?> = runCatching {
        httpClient.delete("/post/$postId/delete")
    }.asApiResponse()

    override suspend fun postComment(postId: Int, request: PostCommentRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/comment/$postId/create"){
            setBody(request)
        }
    }.asApiResponse()

    override suspend fun deleteComment(commentId: Int): ApiResponse<Unit?> = runCatching {
        httpClient.delete("/comment/$commentId/delete")
    }.asApiResponse()

    override suspend fun reportPost(request: ReportPostRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/report/post")
    }.asApiResponse()

    override suspend fun reportComment(request: ReportCommentRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/report/comment")
    }.asApiResponse()

    override suspend fun postLike(postId: Int): ApiResponse<PostLikeResponse> = runCatching {
        httpClient.post("/post/$postId/like")
    }.asApiResponse()
}