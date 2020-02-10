package pudans.trafficconditionmap.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun toISO8601(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return df.format(date)
    }

    fun fromISO860toHumanReadable(dateStr: String?): String? {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        try {
            val date = df.parse(dateStr)
            return SimpleDateFormat("dd MMM HH:mm:ss", Locale.getDefault()).format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }
}