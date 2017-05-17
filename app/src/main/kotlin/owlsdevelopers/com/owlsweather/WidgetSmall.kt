package owlsdevelopers.com.owlsweather


import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import owlsdevelopers.com.owlsweather.data.Town
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class WidgetSmall : AppWidgetProvider() {

    internal val LOG_TAG = "myLogs"

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(LOG_TAG, "onEnabled")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context.startService(Intent(context, UpdateService::class.java))

    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            val ids = intent.extras.getIntArray(WIDGET_IDS_KEY)
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids)
        } else
            super.onReceive(context, intent)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds))
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(LOG_TAG, "onDisabled")
    }

    class UpdateService : Service() {

        override fun onStart(intent: Intent, startId: Int) {
            // Build the widget update for today

            val updateViews = buildUpdate(this)

            // Push update for this widget to the home screen
            val thisWidget = ComponentName(this, WidgetSmall::class.java)
            val manager = AppWidgetManager.getInstance(this)
            manager.updateAppWidget(thisWidget, updateViews)
        }

        fun buildUpdate(context: Context): RemoteViews {
            val intent = Intent(context, HomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, 0)

            val updateViews = RemoteViews(
                    context.packageName, R.layout.widget_small)



            updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent)
            val sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this)
            val languageCode = sharedPrefs.getString("langPref",
                    resources.getString(R.string.defaultLanguage))
            //			final String townCode = sharedPrefs.getString("widgetTownCodePref",
            //					getResources().getString(R.string.defaultTownCode));

            val townCode = sharedPrefs.getString("widgetTownCodePref", "")
            // runOnUiThread(new Runnable() {
            // public void run() {

            Log.d("Weather", "Widget. Town code: " + townCode!!)


            val dm = (context
                    .applicationContext as OwlsWeatherApplication).dataManager

            var town: Town? = null
            if ("".equals(townCode, ignoreCase = true)) {
                if (dm.towns.size > 0) {
                    town = dm.towns[0]
                }
            } else {
                town = dm.getTownByCode(townCode)
            }
            if (town == null) return updateViews
            Log.d("Weather", "Widget. Town name: " + town.townName)
            updateWidgetUI(context, town, updateViews)
            return updateViews
        }

        fun updateWidgetUI(context: Context, town: Town?, updateViews: RemoteViews) {

            if (town != null) {
                updateViews.setTextViewText(R.id.city, town.townName)

                val ts = town.forecast

                if (ts.size >= 1) {

                    updateViews.setTextViewText(R.id.temp1, "" + ts.get(1).temperature)
                    updateViews.setViewVisibility(R.id.image1, View.VISIBLE)
                    updateViews.setImageViewResource(R.id.image1, ts.get(1).cloudImgResId)
                    updateViews.setTextViewText(R.id.timeView1, " " + ts.get(1).shortDate)
                }


            }
        }

        override fun onBind(intent: Intent): IBinder? {
            // We don't need to bind to this service
            return null
        }


        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        @Throws(IOException::class)
        private fun downloadUrl(urlString: String): InputStream {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true
            // Starts the query
            conn.connect()
            val stream = conn.inputStream
            return stream
        }
    }

    companion object {

        val WIDGET_IDS_KEY = "mywidgetproviderwidgetids"

        fun getImageId(context: Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable",
                    context.packageName)
        }
    }

}
