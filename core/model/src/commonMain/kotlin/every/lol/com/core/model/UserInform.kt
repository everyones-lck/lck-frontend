package every.lol.com.core.model

data class UserInform(
    val nickname: String,
    val profileImage: String?=null,
    val teamIds: List<Int>
)