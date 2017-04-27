package owlsdevelopers.com.owlsweather.data.model

import java.io.Serializable

/**
 * Created by admin on 27.06.15.
 */
class WeatherResponce : Serializable {

    var response: Response? = null

    val forecast: Forecast?
        get() = response!!.forecast

    companion object {
        private const val serialVersionUID = 1L
    }

}
