package owlsdevelopers.com.owlsweather.data.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by admin on 27.06.15.
 */
class ForecastItem(val gmt: Date, val gmt_string: String, val cloud_cover: ForecastItem.Cloud_cover, val cloud_cover_hint: ForecastItem.Cloud_cover_hint, val precipitation: ForecastItem.Precipitation, val precipitation_hint: ForecastItem.Precipitation_hint, val precipitation_type: Long, val temperature: ForecastItem.Temperature, val feel_temperature: ForecastItem.Feel_temperature, val humidity: Long, val humidity_hint: String, val pressure: ForecastItem.Pressure, val pressure_hint: String, val wind_velocity: ForecastItem.Wind_velocity, val wind_velocity_hint: String, val wind_direction: Long, val wind_direction_hint: String, val sunrise: Long, val sunset: Long, val moonrise: String, val moonset: String, val moon_phase: Long, val moon_phase_hint: String) : Serializable {

    class Cloud_cover(val pct: Long, val decimal: Long, val oktas: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Cloud_cover_hint(val pct: String, val decimal: String, val oktas: String) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Precipitation(val mm: Double, val inches: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Precipitation_hint(val mm: String, val inches: String) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Temperature(val c: Long, val f: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Feel_temperature(val c: Long, val f: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Pressure(val mmhg: Long, val inhg: Long, val mbar: Long, val hpa: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class Wind_velocity(val ms: Long, val kmh: Long, val mph: Long, val knots: Long, val bft: Long) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    /**
     * UTIL
     */
    val formatedTime: String
        get() {
            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(this.gmt)
        }

    val displayDate: String
        get() {
            val dateFormat = SimpleDateFormat("E, dd")
            return dateFormat.format(this.gmt)
        }

    val displayTime: String
        get() {
            val dateFormat = SimpleDateFormat("E HH:mm")
            return dateFormat.format(this.gmt)
        }

    val displayLongDate: String
        get() {
            val dateFormat = SimpleDateFormat("E, dd MMM HH:mm")
            return dateFormat.format(this.gmt)
        }

    val weekDay: String
        get() {
            val dateFormat = SimpleDateFormat("E")
            return dateFormat.format(this.gmt)
        }

    val isDay: Boolean
        get() {
            val cal = Calendar.getInstance()
            cal.time = this.gmt
            return cal.get(Calendar.HOUR_OF_DAY) < 21 && cal.get(Calendar.HOUR_OF_DAY) > 3
        }

    companion object {

        private const val serialVersionUID = 1L
    }


}

