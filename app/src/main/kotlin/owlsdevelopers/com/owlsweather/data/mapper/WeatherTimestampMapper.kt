package owlsdevelopers.com.owlsweather.data.mapper

import android.content.Context
import com.survivingwithandroid.weather.lib.model.BaseWeather
import com.survivingwithandroid.weather.lib.model.DayForecast
import com.survivingwithandroid.weather.lib.model.WeatherForecast
import com.survivingwithandroid.weather.lib.util.WindDirection
import owlsdevelopers.com.owlsweather.data.FU
import owlsdevelopers.com.owlsweather.data.ui.WeatherTimestep

/**
 * Created by igorka on 4/27/17.
 */
class WeatherTimestampMapper {


    enum class DayTimes {
        DAY, EVE, NIGHT, MORNING
    }

    fun map(context: Context, forecast: WeatherForecast): ArrayList<WeatherTimestep> {
        var timestamps = ArrayList<WeatherTimestep>()


        for(day in forecast.forecast){
            timestamps.add(watherFromDay(context, day, forecast.unit))
//            timestamps.add(watherFromDay(day, DayTimes.MORNING, forecast.unit))
//            timestamps.add(watherFromDay(day, DayTimes.DAY, forecast.unit))
//            timestamps.add(watherFromDay(day, DayTimes.EVE, forecast.unit))
//            timestamps.add(watherFromDay(day, DayTimes.NIGHT, forecast.unit))
        }

        return timestamps;
    }

    fun watherFromDay(context: Context, day: DayForecast/*, time: DayTimes*/, units: BaseWeather.WeatherUnit): WeatherTimestep{
        val timestamp = WeatherTimestep()
        timestamp.timestep = day.timestamp * 1000
        timestamp.windVelocity = "%.1f ${units.speedUnit}".format(day.weather.wind.speed)
        timestamp.windDirection = WindDirection.getDir(day.weather.wind.deg.toInt()).name
        timestamp.temperature = "%.0f ${units.tempUnit}".format(day.forecastTemp.day)
        timestamp.humidity = "%.1f %%".format(day.weather.currentCondition.humidity)
        timestamp.pressure = "%.1f ${units.pressureUnit}".format(day.weather.currentCondition.pressure)
        timestamp.realFeel = "%.0f ${units.tempUnit}".format(day.weather.currentCondition.feelsLike)
        timestamp.shortDate = FU.getDisplayShortDate(timestamp.timestep)
        timestamp.longDate = FU.getDisplayLongDate(timestamp.timestep)
        timestamp.conditionDsc = day.weather.currentCondition.condition
        timestamp.cloudImgResId = WeatherIconMapper.getCloudResource(context, day.weather.currentCondition.icon)
        timestamp.precipitationImgResId = WeatherIconMapper.getPrecipitationResource(context, day.weather.currentCondition.icon)



//        when(time){
//            DayTimes.MORNING -> {
//                timestamp.temperature = "" + day.forecastTemp.morning + units.tempUnit
//                timestamp.datetime = "morning"
//            }
//            DayTimes.DAY -> {
//                timestamp.temperature = "" + day.forecastTemp.day + units.tempUnit
//                timestamp.datetime = "day"
//            }
//            DayTimes.EVE -> {
//                timestamp.temperature = "" + day.forecastTemp.eve + units.tempUnit
//                timestamp.datetime = "eve"
//            }
//            DayTimes.NIGHT -> {
//                timestamp.temperature = "" + day.forecastTemp.night + units.tempUnit
//                timestamp.datetime = "night"
//            }
//        }
//


        return timestamp
    }


}
