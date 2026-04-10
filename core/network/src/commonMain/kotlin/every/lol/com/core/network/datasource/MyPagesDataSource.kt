package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PatchMyTeamRequest
import every.lol.com.core.network.model.request.PatchProfileRequest
import every.lol.com.core.network.model.response.CommentsResponse
import every.lol.com.core.network.model.response.PatchProfileResponse
import every.lol.com.core.network.model.response.PostsResponse
import every.lol.com.core.network.model.response.ProfileResponse
import every.lol.com.core.network.model.response.mypage.GetPogResponse
import every.lol.com.core.network.model.response.mypage.GetPomResponse
import every.lol.com.core.network.model.response.mypage.GetPredictionsRespponse

interface MyPagesDataSource {
    suspend fun getProfile(): ApiResponse<ProfileResponse>
    suspend fun patchProfile(request: PatchProfileRequest): ApiResponse<PatchProfileResponse>
    suspend fun patchMyTeam(request: PatchMyTeamRequest): ApiResponse<Unit?>
    suspend fun getPosts(page: Int, size: Int):ApiResponse<PostsResponse>
    suspend fun getComments(page: Int, size: Int):ApiResponse<CommentsResponse>
    suspend fun withdrawal(): ApiResponse<Unit?>
    suspend fun logout(refreshToken: String): ApiResponse<Unit?>
    suspend fun getPredictions(): ApiResponse<GetPredictionsRespponse>
    suspend fun getPog(): ApiResponse<GetPogResponse>
    suspend fun getPom(): ApiResponse<GetPomResponse>
}