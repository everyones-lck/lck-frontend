package every.lol.com.core.common

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneWithName

actual fun formatMillisToDate(millis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)

    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd"
        timeZone = NSTimeZone.timeZoneWithName("UTC")!!
    }

    return formatter.stringFromDate(date)
}