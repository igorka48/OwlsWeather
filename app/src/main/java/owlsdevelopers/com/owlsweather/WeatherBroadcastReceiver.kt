package owlsdevelopers.com.owlsweather

import android.content.*

class WeatherBroadcastReceiver(val callback: WeatherLoaderCallback) : BroadcastReceiver() {

    interface WeatherLoaderCallback {
        fun loadingStart()
        fun loadingCompleted()
    }


    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PARAM_STATUS, 0)
        when (status) {
            1 -> callback.loadingStart()
            2 -> callback.loadingCompleted()
        }
    }

    companion object {
        val WEATHER_LOADING_MESSAGE = "WEATHER_LOADING_MESSAGE"
        val PARAM_STATUS = "PARAM_STATUS"

        fun sing(context: Context,  callback: WeatherLoaderCallback): WeatherBroadcastReceiver {
            var br: WeatherBroadcastReceiver = WeatherBroadcastReceiver(callback)
            val intentFilter = IntentFilter(WEATHER_LOADING_MESSAGE);
            context.registerReceiver(br, intentFilter)
            return br
        }

        fun unsing(context: Context, br: WeatherBroadcastReceiver?) {
            context.unregisterReceiver(br)
        }

        fun sendLoadinStart(context: Context) {
            val intent = Intent(WEATHER_LOADING_MESSAGE)
            intent.putExtra(PARAM_STATUS, 1)
            context.sendBroadcast(intent)
        }

        fun sendLoadingCompleted(context: Context) {
            val intent = Intent(WEATHER_LOADING_MESSAGE)
            intent.putExtra(PARAM_STATUS, 2)
            context.sendBroadcast(intent)
        }
    }
}
