package every.lol.com.core.common

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.text.format
import java.time.Instant


actual fun formatMillisToDate(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val date = instant.atZone(ZoneId.of("UTC")).toLocalDate()
    return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
}