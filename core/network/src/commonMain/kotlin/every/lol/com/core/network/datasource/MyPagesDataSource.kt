package every.lol.com.core.network.datasource

import every.lol.com.core.network.model.ApiResponse
import every.lol.com.core.network.model.request.PatchMyTeamRequest
import every.lol.com.core.network.model.request.PatchProfileRequest
import every.lol.com.core.network.model.response.PatchProfileResponse
import every.lol.com.core.network.model.response.ProfileResponse

interface MyPagesDataSource {
    suspend fun getProfile(): ApiResponse<ProfileResponse>
    suspend fun patchProfile(request: PatchProfileRequest): ApiResponse<PatchProfileResponse>
    suspend fun patchMyTeam(request: PatchMyTeamRequest): ApiResponse<Unit?>
}