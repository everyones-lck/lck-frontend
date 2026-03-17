package every.lol.com.core.domain.repository

import every.lol.com.core.model.UserInform

interface AuthRepository {

    suspend fun login(kakaoUserId: String): Result<Unit>
    suspend fun signup(request: UserInform): Result<Unit>
    suspend fun refresh(kakaoUserId: String): Result<Unit>
    suspend fun nickname(nickname: String): Result<Boolean?>
    suspend fun getValidAccessToken(): String?

}