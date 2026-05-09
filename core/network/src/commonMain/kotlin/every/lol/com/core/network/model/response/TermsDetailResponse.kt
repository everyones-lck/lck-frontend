package every.lol.com.core.network.model.response

import kotlinx.serialization.Serializable


@Serializable
data class TermsDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    val isRequired: Boolean
)