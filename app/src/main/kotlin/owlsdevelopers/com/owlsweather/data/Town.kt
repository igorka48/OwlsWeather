package owlsdevelopers.com.owlsweather.data

import com.survivingwithandroid.weather.lib.model.DayForecast
import com.survivingwithandroid.weather.lib.model.WeatherForecast

data class Town(var townName: String = "",
                var title: String = "",
                var townCode: String = "",
                var lastUpdateTimestamp: Long = 0) {

    val forecastItems: List<DayForecast>
        get() {
            return forecast?.forecast ?: ArrayList<DayForecast>()
        }

    var forecast: WeatherForecast? = null
       set(value) {
           lastUpdateTimestamp = System.currentTimeMillis()
           field = value
       }
}
