package every.lol.com.core.domain.repository

import every.lol.com.core.model.Profile

interface MyPagesRepository {
    suspend fun getProfile(): Result<Profile>
    suspend fun patchProfile(nickname: String, profileImage: ByteArray?): Result<Unit>

}