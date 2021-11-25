package pudans.trafficconditionmap.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

	fun toISO8601(date: Date): String =
		SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(date)

	fun fromISO860toHumanReadable(dateStr: String?): String? {
		val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
		val date = df.parse(dateStr ?: "")
		date ?: return null
		return SimpleDateFormat("dd MMM HH:mm:ss", Locale.getDefault()).format(date)
	}
}
