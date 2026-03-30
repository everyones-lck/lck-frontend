package every.lol.com.core.domain.repository

import every.lol.com.core.model.Comments
import every.lol.com.core.model.Posts
import every.lol.com.core.model.UserInform

interface MyPagesRepository {
    suspend fun getProfile(): Result<UserInform>
    suspend fun patchProfile(nickname: String, profileImage: ByteArray?): Result<Unit>
    suspend fun patchMyTeam(teamIds: List<Int>): Result<Unit>
    suspend fun getPosts(page: Int): Result<Posts>
    suspend fun getComments(page: Int): Result<Comments>
    suspend fun withdrawal(): Result<Unit?>
    suspend fun logout(): Result<Unit?>
}