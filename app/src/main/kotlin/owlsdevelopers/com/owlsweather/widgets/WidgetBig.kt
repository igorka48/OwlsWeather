package owlsdevelopers.com.owlsweather.widgets

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

import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.R

import java.util.Arrays

import owlsdevelopers.com.owlsweather.data.model.Town
import owlsdevelopers.com.owlsweather.ui.MainActivity
import owlsdevelopers.com.owlsweather.ui.repository.WeatherRepository


class WidgetBig : AppWidgetProvider() {

    internal val LOG_TAG = "myLogs"

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(LOG_TAG, "onEnabled")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context.startService(Intent(context, UpdateService::class.java))

    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.hasExtra(WIDGET_IDS_KEY) ?: false) {
            val ids = intent?.extras?.getIntArray(WIDGET_IDS_KEY)
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

        //internal var handler: Handler

        override fun onStart(intent: Intent, startId: Int) {
            // Build the widget update for today

            val updateViews = buildUpdate(this)

            // Push update for this widget to the home screen
            val thisWidget = ComponentName(this, WidgetBig::class.java)
            val manager = AppWidgetManager.getInstance(this)
            manager.updateAppWidget(thisWidget, updateViews)
        }

        fun buildUpdate(context: Context): RemoteViews {
            //handler = Handler()
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, 0)

            val weatherRepository: WeatherRepository

            val updateViews = RemoteViews(
                    context.packageName, R.layout.widget_big)


            updateViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
            val sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this)

            val townCode = sharedPrefs.getString("widgetTownCodePref", "")

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


            try {
                if (town != null) {
                    updateViews.setTextViewText(R.id.city, town.townName)


                    val ts = town.forecast

                    if (ts.size > 1) {

                        updateViews.setTextViewText(R.id.temp1, "" + ts.get(1).temperature)
                        updateViews.setViewVisibility(R.id.image1, View.VISIBLE)
                        updateViews.setImageViewResource(R.id.image1, ts.get(1).cloudImgResId)
                        updateViews.setTextViewText(R.id.timeView1, " " + ts.get(1).shortDate)


                    }

                    if (ts.size > 3) {
                        updateViews.setTextViewText(R.id.temp2, "" + ts.get(3).temperature)
                        updateViews.setViewVisibility(R.id.image2, View.VISIBLE)
                       // updateViews.setTextViewText(R.id.textView3, ts.get(3).getWeekDay())
                        updateViews.setImageViewResource(R.id.image2, ts.get(3).cloudImgResId)
                        updateViews.setTextViewText(R.id.timeView2, " " + ts.get(3).shortDate)

                    }
                    if (ts.size > 5) {
                        updateViews.setTextViewText(R.id.temp3, "" + ts.get(5).temperature)
                        updateViews.setViewVisibility(R.id.image3, View.VISIBLE)
                        // updateViews.setTextViewText(R.id.textView3, ts.get(5).getWeekDay())
                        updateViews.setImageViewResource(R.id.image3, ts.get(5).cloudImgResId)
                        updateViews.setTextViewText(R.id.timeView3, " " + ts.get(5).shortDate)

                    }
                    if (ts.size > 7) {
                        updateViews.setTextViewText(R.id.temp4, "" + ts.get(7).temperature)
                        updateViews.setViewVisibility(R.id.image4, View.VISIBLE)
                        // updateViews.setTextViewText(R.id.textView4, ts.get(7).getWeekDay())
                        updateViews.setImageViewResource(R.id.image4, ts.get(7).cloudImgResId)
                        updateViews.setTextViewText(R.id.timeView4, " " + ts.get(7).shortDate)

                    }
                }

            } catch (ex: Exception) {
                Log.e("Weather", "Error: " + ex.localizedMessage)
            }

        }

        override fun onBind(intent: Intent): IBinder? {
            // We don't need to bind to this service
            return null
        }

        private fun runOnUiThread(runnable: Runnable) {
           // handler.postDelayed(runnable, 100)

        }

    }

    companion object {

        val WIDGET_IDS_KEY = "mybigwidgetproviderwidgetids"

        fun getImageId(context: Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable",
                    context.packageName)
        }
    }

}
