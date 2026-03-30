package every.lol.com.core.model

data class Signup(
    val kakaoUserId: String,
    val nickname: String,
    val profileImage: ByteArray?=null,
    val teamIds: List<Int>
)