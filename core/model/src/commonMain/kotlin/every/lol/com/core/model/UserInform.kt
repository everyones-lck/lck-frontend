package every.lol.com.core.model

data class UserInform(
    val kakaoUserId: String,
    val nickname: String,
    val profileImage: String?=null,
    val teamId: List<Int>
)