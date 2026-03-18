package every.lol.com.core.model

data class Posts(
    val posts: List<PostsDetail>,
    val isLast: Boolean
)

data class PostsDetail(
    val id: Int,
    val title: String,
    val postType: String
)