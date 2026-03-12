package every.lol.com.core.model

data class Signup(
    val kakaoUserId: String,
    val nickname: String,
    val profileImage: Any?=null,
    val teamId: List<Int>
)