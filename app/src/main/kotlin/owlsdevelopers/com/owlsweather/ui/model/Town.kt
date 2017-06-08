package owlsdevelopers.com.owlsweather.ui.model

import android.location.Location

data class Town(var townName: String = "",
                var title: String = "",
                var townCode: String = "",
                var lastUpdateTimestamp: Long = 0) {

    var forecast: Array<WeatherTimestep> = arrayOf()
       set(value) {
           lastUpdateTimestamp = System.currentTimeMillis()
           field = value
       }

    var location: Location? = null
}
