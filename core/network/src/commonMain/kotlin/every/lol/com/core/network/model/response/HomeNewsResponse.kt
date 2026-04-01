package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeNewsResponse(
    val newsList: List<HomeNewsDetailResponse>
)

@Serializable
data class HomeNewsDetailResponse(
    val title: String,
    val link: String,
    val press: String,
    val publishedAt: String,
    val thumbnailUrl: String
)