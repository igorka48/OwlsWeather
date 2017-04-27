package owlsdevelopers.com.owlsweather.data.ui


data class WeatherTimestep(var temperature: String = "0", var humidity: String = "0", var pressure: String = "", var windDirection: String = "") {

    val formatedTime: String
        get() {
            var ret = "" + localTime + ":00"
            if (ret.length == 4) {
                ret = "0" + ret
            }
            return ret

        }

    var realFeel: String = "0"

    var shortDate: String = ""
    var longDate: String = ""


    var timestep: Long = 0

    var datetime: String? = null

    var g: Int = 0

    var hHii: String? = null

    var cloud_cover: Int = 0

    var precipitation: Float = 0.toFloat()


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
