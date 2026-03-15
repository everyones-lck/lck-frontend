package every.lol.com.core.model


data class AboutLCKTeamHistory(
    val seasonNameList: List<String>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)