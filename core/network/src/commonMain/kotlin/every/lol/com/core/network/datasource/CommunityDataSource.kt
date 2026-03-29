package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PostCommentRequest
import every.lol.com.core.network.model.request.PostPostDetailRequest
import every.lol.com.core.network.model.request.PostPostRequest
import every.lol.com.core.network.model.request.ReportCommentRequest
import every.lol.com.core.network.model.request.ReportPostRequest
import every.lol.com.core.network.model.response.PostDetailResponse
import every.lol.com.core.network.model.response.PostIdResponse
import every.lol.com.core.network.model.response.PostListResponse

interface CommunityDataSource {
    suspend fun postPost(request: PostPostRequest): ApiResponse<PostIdResponse>
    suspend fun editPost(postId: Int, request: PostPostDetailRequest): ApiResponse<PostIdResponse>
    suspend fun detailPost(postId: Int): ApiResponse<PostDetailResponse>
    suspend fun postList(postType: String, page: Int, size: Int): ApiResponse<PostListResponse>
    suspend fun deletePost(postId: Int): ApiResponse<Unit?>
    suspend fun postComment(postId: Int, request: PostCommentRequest): ApiResponse<Unit?>
    suspend fun deleteComment(commentId: Int): ApiResponse<Unit?>
    suspend fun reportPost(request: ReportPostRequest): ApiResponse<Unit?>
    suspend fun reportComment(request: ReportCommentRequest): ApiResponse<Unit?>
}