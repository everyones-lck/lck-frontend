package every.lol.com.core.network.remote

import every.lol.com.core.network.datasource.CommunityDataSource
import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PostCommentRequest
import every.lol.com.core.network.model.request.PostPostDetailRequest
import every.lol.com.core.network.model.request.PostPostRequest
import every.lol.com.core.network.model.request.ReportCommentRequest
import every.lol.com.core.network.model.request.ReportPostRequest
import every.lol.com.core.network.model.response.PostDetailResponse
import every.lol.com.core.network.model.response.PostIdResponse
import every.lol.com.core.network.model.response.PostListResponse
import every.lol.com.core.network.util.asApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class CommunityDataSourceImpl(
    private val httpClient: HttpClient
): CommunityDataSource {

    override suspend fun postPost(request: PostPostRequest): ApiResponse<PostIdResponse> = runCatching {
        httpClient.post("/post/create"){
            setBody(request)
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

    override suspend fun deletePost(postId: Int): ApiResponse<Unit?> = runCatching {
        httpClient.delete("/post/$postId/delete")
    }.asApiResponse()

    override suspend fun postComment(postId: Int, request: PostCommentRequest): ApiResponse<Unit?> = runCatching {
        httpClient.post("/comment/$postId/create")
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
}