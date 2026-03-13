package every.lol.com.core.data.repository

import every.lol.com.core.data.mapper.toResult
import every.lol.com.core.datastore.AuthLocalDataSource
import every.lol.com.core.domain.repository.MyPagesRepository
import every.lol.com.core.model.Profile
import every.lol.com.core.network.datasource.MyPagesDataSource
import every.lol.com.core.network.model.request.PatchProfileData
import every.lol.com.core.network.model.request.PatchProfileRequest

class MyPageRepositoryImpl(
    private val remote: MyPagesDataSource,
    private val local: AuthLocalDataSource
): MyPagesRepository {
    override suspend fun getProfile(): Result<Profile> =
        remote.getProfile().toResult().mapCatching { response ->
            Profile(
                nickname = response.nickname,
                profileImageUrl = response.profileImage,
                teamId = response.teamId,
                tier = response.tier
            )
        }

    override suspend fun patchProfile(nickname: String, profileImage: ByteArray?): Result<Unit> {
        val isDefaultImage = if (profileImage == null) true else false

        return remote.patchProfile(
            request = PatchProfileRequest(
                profileImage = profileImage,
                request = PatchProfileData(nickname = nickname, isDefaultImage = isDefaultImage)
            )
        ).toResult().map {
            Unit
        }
    }
}