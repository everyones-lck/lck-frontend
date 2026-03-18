package every.lol.com.core.domain.repository

import every.lol.com.core.model.Comments
import every.lol.com.core.model.Posts
import every.lol.com.core.model.Profile

interface MyPagesRepository {
    suspend fun getProfile(): Result<Profile>
    suspend fun patchProfile(nickname: String, profileImage: ByteArray?=null): Result<Unit>
    suspend fun getPosts(page: Int, size: Int): Result<Posts>
    suspend fun getComments(page: Int, size: Int): Result<Comments>
    suspend fun withdrawal(): Result<Unit?>
    suspend fun logout(): Result<Unit?>
}