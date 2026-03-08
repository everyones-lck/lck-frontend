package every.lol.com.core.model

import everylol.core.model.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class TosType(
    val id: Int,
    val title: String,
    val content: StringResource
) {
    SERVICE_TERM(1, "[필수] 서비스 이용약관", Res.string.tos_details_agree_1),
    PRIVACY_POLICY(2, "[필수] 개인정보 처리방침",Res.string.tos_details_agree_2);

    companion object {
        fun fromId(id: Int?): TosType = values().find { it.id == id } ?: SERVICE_TERM
    }
}