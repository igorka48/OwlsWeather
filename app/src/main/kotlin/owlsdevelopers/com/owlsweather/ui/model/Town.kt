package owlsdevelopers.com.owlsweather.ui.model

import android.location.Location
import owlsdevelopers.com.owlsweather.ui.model.WeatherTimestep

data class Town(var townName: String = "",
                var title: String = "",
                var townCode: String = "",
                var lastUpdateTimestamp: Long = 0) {

    var forecast: ArrayList<owlsdevelopers.com.owlsweather.ui.model.WeatherTimestep> = ArrayList()
       set(value) {
           lastUpdateTimestamp = System.currentTimeMillis()
           field = value
       }

    var location: android.location.Location? = null
}
