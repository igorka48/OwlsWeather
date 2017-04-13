package owlsdevelopers.com.owlsweather.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by igorka on 07.03.17.
 */

class FU {

    fun getDisplayDate(time: Date): String {
        val dateFormat = SimpleDateFormat("E, dd")
        return dateFormat.format(time)
    }

    fun getDisplayTime(time: Date): String {
        val dateFormat = SimpleDateFormat("E HH:mm")
        return dateFormat.format(time)
    }

    fun getWeekDay(time: Date): String {
        val dateFormat = SimpleDateFormat("E")
        return dateFormat.format(time)
    }

    companion object {
        /**
         * UTIL
         */
        fun getFormatedTime(time: Date): String {
            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(time)
        }

        fun getDisplayLongDate(time: Long): String {
            val dateFormat = SimpleDateFormat("E, dd MMM HH:mm")
            return dateFormat.format(time)
        }

        fun isDay(time: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = time
            return cal.get(Calendar.HOUR_OF_DAY) < 21 && cal.get(Calendar.HOUR_OF_DAY) > 3
        }
    }
}
