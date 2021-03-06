package owlsdevelopers.com.owlsweather

import android.content.*
import android.support.v4.content.LocalBroadcastManager

class WeatherBroadcastReceiver(var callback: WeatherLoaderCallback) : BroadcastReceiver() {

    interface WeatherLoaderCallback {
        fun loadingStart()
        fun loadingCompleted()
        fun loadingError(message: String)
    }


    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PARAM_STATUS, 0)
        val message = intent.getStringExtra(PARAM_MESSAGE)
        when (status) {
            1 -> callback.loadingStart()
            2 -> callback.loadingCompleted()
            3 -> callback.loadingError(message)
        }
    }

    companion object {
        val WEATHER_LOADING_MESSAGE = "WEATHER_LOADING_MESSAGE"
        val PARAM_STATUS = "PARAM_STATUS"
        val PARAM_MESSAGE = "PARAM_MESSAGE"


        fun sign(context: Context,  callback: WeatherLoaderCallback): WeatherBroadcastReceiver {
            var br: WeatherBroadcastReceiver = WeatherBroadcastReceiver(callback)
            val intentFilter = IntentFilter(WEATHER_LOADING_MESSAGE)
            LocalBroadcastManager.getInstance(context).registerReceiver(br, intentFilter)
            return br
        }

        fun unsign(context: Context, br: WeatherBroadcastReceiver?) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(br)
        }

        fun sendLoadinStart(context: Context) {
            val intent = Intent(WEATHER_LOADING_MESSAGE)
            intent.putExtra(PARAM_STATUS, 1)
            sendBroadcast(context, intent)
        }

        fun sendLoadingCompleted(context: Context) {
            val intent = Intent(WEATHER_LOADING_MESSAGE)
            intent.putExtra(PARAM_STATUS, 2)
            sendBroadcast(context, intent)
        }

        fun sendLoadingError(context: Context, error: Throwable) {
            val intent = Intent(WEATHER_LOADING_MESSAGE)
            intent.putExtra(PARAM_STATUS, 2)
            intent.putExtra(PARAM_MESSAGE, error.localizedMessage)
            sendBroadcast(context, intent)
        }

        private fun sendBroadcast(context: Context, intent: Intent){
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

    }
}
