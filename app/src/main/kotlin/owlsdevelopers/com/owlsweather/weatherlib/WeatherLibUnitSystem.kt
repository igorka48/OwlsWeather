package owlsdevelopers.com.owlsweather.weatherlib

import com.survivingwithandroid.weather.lib.WeatherConfig
import owlsdevelopers.com.owlsweather.ui.model.UnitSystem

/**
 * Created by igorka on 6/5/17.
 */
class  WeatherLibUnitSystem(key: String, dsc: String) : UnitSystem(key, dsc){



    fun unitSystemValue(): WeatherConfig.UNIT_SYSTEM {
        when(key){
             METRIC_KEY -> return WeatherConfig.UNIT_SYSTEM.M
             IMPERIAL_KEY -> return WeatherConfig.UNIT_SYSTEM.I
        }
        return WeatherConfig.UNIT_SYSTEM.M
    }

    companion object{
        //* M = Metric system
        //* I = Imperial System
        private val METRIC_KEY = "M"
        private val IMPERIAL_KEY = "I"
        private val METRIC_DSC = "Metric system"
        private val IMPERIAL_DSC = "Imperial System"

        fun  Metric(): UnitSystem {
            return WeatherLibUnitSystem(METRIC_KEY, METRIC_DSC)
        }
        fun  Imperial(): UnitSystem {
            return WeatherLibUnitSystem(IMPERIAL_KEY, IMPERIAL_DSC)
        }

        fun unitSystemById(key: String): UnitSystem {
            when(key){
                WeatherLibUnitSystem.METRIC_KEY -> return Metric()
                WeatherLibUnitSystem.IMPERIAL_KEY -> return Imperial()
            }
            return Metric()
        }

    }
}
