package owlsdevelopers.com.owlsweather.data

import owlsdevelopers.com.owlsweather.data.ui.WeatherTimestep

data class Town(var townName: String = "",
                var title: String = "",
                var townCode: String = "",
                var lastUpdateTimestamp: Long = 0) {

    var forecast: ArrayList<WeatherTimestep> = ArrayList()
       set(value) {
           lastUpdateTimestamp = System.currentTimeMillis()
           field = value
       }
}
