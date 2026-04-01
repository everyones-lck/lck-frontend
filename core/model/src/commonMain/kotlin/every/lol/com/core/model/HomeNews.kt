package every.lol.com.core.model

data class HomeNews(
    val newsList: List<HomeNewsDetail>
)

data class HomeNewsDetail(
    val title: String,
    val link: String,
    val press: String,
    val publishedAt: String,
    val thumbnailUrl: String
)