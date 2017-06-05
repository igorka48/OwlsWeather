package owlsdevelopers.com.owlsweather.widgets

import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.data.model.Town
import owlsdevelopers.com.owlsweather.ui.HomeActivity


class WidgetSmall : android.appwidget.AppWidgetProvider() {

    internal val LOG_TAG = "myLogs"

    override fun onEnabled(context: android.content.Context) {
        super.onEnabled(context)
        android.util.Log.d(LOG_TAG, "onEnabled")
    }

    override fun onUpdate(context: android.content.Context, appWidgetManager: android.appwidget.AppWidgetManager,
                          appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context.startService(android.content.Intent(context, UpdateService::class.java))

    }

    override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
        if (intent.hasExtra(owlsdevelopers.com.owlsweather.widgets.WidgetSmall.Companion.WIDGET_IDS_KEY)) {
            val ids = intent.extras.getIntArray(owlsdevelopers.com.owlsweather.widgets.WidgetSmall.Companion.WIDGET_IDS_KEY)
            this.onUpdate(context, android.appwidget.AppWidgetManager.getInstance(context), ids)
        } else
            super.onReceive(context, intent)
    }

    override fun onDeleted(context: android.content.Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        android.util.Log.d(LOG_TAG, "onDeleted " + java.util.Arrays.toString(appWidgetIds))
    }

    override fun onDisabled(context: android.content.Context) {
        super.onDisabled(context)
        android.util.Log.d(LOG_TAG, "onDisabled")
    }

    class UpdateService : android.app.Service() {

        override fun onStart(intent: android.content.Intent, startId: Int) {
            // Build the widget update for today

            val updateViews = buildUpdate(this)

            // Push update for this widget to the home screen
            val thisWidget = android.content.ComponentName(this, WidgetSmall::class.java)
            val manager = android.appwidget.AppWidgetManager.getInstance(this)
            manager.updateAppWidget(thisWidget, updateViews)
        }

        fun buildUpdate(context: android.content.Context): android.widget.RemoteViews {
            val intent = android.content.Intent(context, HomeActivity::class.java)
            val pendingIntent = android.app.PendingIntent.getActivity(context, 0,
                    intent, 0)

            val updateViews = android.widget.RemoteViews(
                    context.packageName, R.layout.widget_small)



            updateViews.setOnClickPendingIntent(owlsdevelopers.com.owlsweather.R.id.widget, pendingIntent)
            val sharedPrefs = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(this)
            val languageCode = sharedPrefs.getString("langPref",
                    resources.getString(owlsdevelopers.com.owlsweather.R.string.defaultLanguage))
            //			final String townCode = sharedPrefs.getString("widgetTownCodePref",
            //					getResources().getString(R.string.defaultTownCode));

            val townCode = sharedPrefs.getString("widgetTownCodePref", "")
            // runOnUiThread(new Runnable() {
            // public void run() {

            android.util.Log.d("Weather", "Widget. Town code: " + townCode!!)


            val dm = (context
                    .applicationContext as owlsdevelopers.com.owlsweather.OwlsWeatherApplication).dataManager

            var town: Town? = null
            if ("".equals(townCode, ignoreCase = true)) {
                if (dm.towns.size > 0) {
                    town = dm.towns[0]
                }
            } else {
                town = dm.getTownByCode(townCode)
            }
            if (town == null) return updateViews
            android.util.Log.d("Weather", "Widget. Town name: " + town.townName)
            updateWidgetUI(context, town, updateViews)
            return updateViews
        }

        fun updateWidgetUI(context: android.content.Context, town: Town?, updateViews: android.widget.RemoteViews) {

            if (town != null) {
                updateViews.setTextViewText(owlsdevelopers.com.owlsweather.R.id.city, town.townName)

                val ts = town.forecast

                if (ts.size >= 1) {

                    updateViews.setTextViewText(owlsdevelopers.com.owlsweather.R.id.temp1, "" + ts.get(1).temperature)
                    updateViews.setViewVisibility(owlsdevelopers.com.owlsweather.R.id.image1, android.view.View.VISIBLE)
                    updateViews.setImageViewResource(owlsdevelopers.com.owlsweather.R.id.image1, ts.get(1).cloudImgResId)
                    updateViews.setTextViewText(owlsdevelopers.com.owlsweather.R.id.timeView1, " " + ts.get(1).shortDate)
                }


            }
        }

        override fun onBind(intent: android.content.Intent): android.os.IBinder? {
            // We don't need to bind to this service
            return null
        }


    }

    companion object {

        val WIDGET_IDS_KEY = "mywidgetproviderwidgetids"

        fun getImageId(context: android.content.Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable",
                    context.packageName)
        }
    }

}
