package every.lol.com.core.domain.repository

import every.lol.com.core.model.Signup

interface AuthRepository {

    suspend fun login(kakaoUserId: String): Result<Unit>
    suspend fun signup(request: Signup): Result<Unit>
    suspend fun refresh(kakaoUserId: String): Result<Unit>
    suspend fun nickname(nickname: String): Result<Boolean?>

}