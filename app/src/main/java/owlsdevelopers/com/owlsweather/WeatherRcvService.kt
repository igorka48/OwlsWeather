package owlsdevelopers.com.owlsweather

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import com.survivingwithandroid.weather.lib.WeatherClient
import com.survivingwithandroid.weather.lib.exception.WeatherLibException
import com.survivingwithandroid.weather.lib.model.WeatherForecast
import com.survivingwithandroid.weather.lib.request.WeatherRequest
import owlsdevelopers.com.owlsweather.data.mapper.WeatherTimestampMapper
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class WeatherRcvService : IntentService("WeatherRcvService") {
    private var _weatherClient: WeatherClient? = null


    val weatherClient: WeatherClient
        get() {
            return _weatherClient ?: WeatherContext.getInstance().getClient(this)
        }


    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val cityId = intent.getStringExtra(CITY_ID)
            val forceUpdate = intent.getBooleanExtra(FORCE_UPDATE, false)

            loadWeatherCall(cityId, forceUpdate)

        }
    }


    private fun loadWeatherCall(cityId: String, forceUpdate: Boolean) {



        WeatherBroadcastReceiver.sendLoadinStart(this)
        val req = WeatherRequest(cityId)
        weatherClient.getForecastWeather(req, object : WeatherClient.ForecastWeatherEventListener {
            override fun onWeatherRetrieved(forecast: WeatherForecast) {



                val data = (this@WeatherRcvService.applicationContext as OwlsWeatherApplication).dataManager

                val town = data.getTownByCode(cityId)

                town?.forecast = WeatherTimestampMapper().map(forecast)

                data.save(this@WeatherRcvService)

                try {
                    val man = AppWidgetManager.getInstance(this@WeatherRcvService)
                    val ids = man.getAppWidgetIds(ComponentName(this@WeatherRcvService,
                            WidgetSmall::class.java))
                    val updateIntent = Intent()
                    updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    updateIntent.putExtra(WidgetSmall.WIDGET_IDS_KEY, ids)
                    sendBroadcast(updateIntent)

                    val idsB = man.getAppWidgetIds(ComponentName(this@WeatherRcvService,
                            WidgetBig::class.java))
                    val updateIntentB = Intent()
                    updateIntentB.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    updateIntentB.putExtra(WidgetBig.WIDGET_IDS_KEY, idsB)
                    sendBroadcast(updateIntentB)

                } catch (ex: Exception) {

                }

                WeatherBroadcastReceiver.sendLoadingCompleted(this@WeatherRcvService)
            }

            override fun onWeatherError(wle: WeatherLibException) {}

            override fun onConnectionError(t: Throwable) {}
        })
    }


    companion object {

        private val CITY_ID = "owlsdevelopers.com.owlsweather.extra.CITY_ID"
        private val FORCE_UPDATE = "owlsdevelopers.com.owlsweather.extra.FORCE_UPDATE"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.

         * @see IntentService
         */
        fun loadWeather(context: Context, cityId: String) {
            val intent = Intent(context, WeatherRcvService::class.java)
            intent.putExtra(CITY_ID, cityId)
            context.startService(intent)
        }
    }
}
