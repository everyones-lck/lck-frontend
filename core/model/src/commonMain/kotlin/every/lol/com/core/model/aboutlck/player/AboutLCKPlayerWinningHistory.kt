package every.lol.com.core.model.aboutlck.player

data class AboutLCKPlayerWinningHistory(
    val seasonNames: List<String>,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)