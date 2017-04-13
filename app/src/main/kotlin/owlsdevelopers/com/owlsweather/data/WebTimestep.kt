package owlsdevelopers.com.owlsweather.data

import java.io.Serializable


class WebTimestep : Serializable {

    val formatedTime: String
        get() {
            var ret = "" + localTime + ":00"
            if (ret.length == 4) {
                ret = "0" + ret
            }
            return ret

        }

    val displayDate: String
        get() = "" + weekDay + ", " + monthDay

    var time_step: Int = 0

    var datetime: String? = null

    var g: Int = 0

    var hHii: String? = null

    var cloud_cover: Int = 0

    var precipitation: Float = 0.toFloat()

    var pressure: String? = null

    var temperature: String? = null

    var humidity: Int = 0

    var wind_direction: String? = null

    var wind_velocity: String? = null

    var falls: Int = 0

    var drops: Float = 0.toFloat()

    var weekDay: String? = null

    var monthDay: String? = null

    var cloudImg: String? = null
    var fallingsImg: String? = null

    var localTime: Int = 0

    companion object {

        const val serialversionuid = 1L
    }


}
