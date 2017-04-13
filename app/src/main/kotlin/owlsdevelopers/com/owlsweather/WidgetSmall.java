package owlsdevelopers.com.owlsweather;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import owlsdevelopers.com.owlsweather.data.DataManager;
import owlsdevelopers.com.owlsweather.data.ForecastUIHelper;
import owlsdevelopers.com.owlsweather.data.Town;
import owlsdevelopers.com.owlsweather.data.model.ForecastItem;

public class WidgetSmall extends AppWidgetProvider {

	final String LOG_TAG = "myLogs";

	public static final String WIDGET_IDS_KEY = "mywidgetproviderwidgetids";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.d(LOG_TAG, "onEnabled");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		context.startService(new Intent(context, UpdateService.class));

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(WIDGET_IDS_KEY)) {
			int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
			this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
		} else
			super.onReceive(context, intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.d(LOG_TAG, "onDisabled");
	}

	public static class UpdateService extends Service {

		Handler handler;

		@Override
		public void onStart(Intent intent, int startId) {
			// Build the widget update for today

			RemoteViews updateViews = buildUpdate(this);

			// Push update for this widget to the home screen
			ComponentName thisWidget = new ComponentName(this, WidgetSmall.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);
		}

		public RemoteViews buildUpdate(Context context) {
			handler = new Handler();
			Intent intent = new Intent(context, HomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			final RemoteViews updateViews = new RemoteViews(
					context.getPackageName(), R.layout.widget_small);
	
			
			
			updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			final String languageCode = sharedPrefs.getString("langPref",
					getResources().getString(R.string.defaultLanguage));
//			final String townCode = sharedPrefs.getString("widgetTownCodePref",
//					getResources().getString(R.string.defaultTownCode));
			
			final String townCode = sharedPrefs.getString("widgetTownCodePref", "");
			// runOnUiThread(new Runnable() {
			// public void run() {

			Log.d("Weather", "Widget. Town code: " + townCode);
			
			
			DataManager dm = ((OwlsWeatherApplication) context
					.getApplicationContext()).getDataManager();
			
			Town town = null;
			if("".equalsIgnoreCase(townCode)){
				if(dm.getTowns().size() > 0){
					town = dm.getTowns().get(0);
				}
			} else {
				town = dm.getTownByCode(townCode);
			}
			if(town == null)return updateViews;
			Log.d("Weather", "Widget. Town name: " + town.getTownName());
			updateWidgetUI(context, town, updateViews);
			final Context fContext = context;
			final Town fTown = town;
//			Rp5SiteParser parser = new Rp5SiteParser(context, town, languageCode, true);
//			parser.parse(new Rp5SiteParserCallBack() {
//
//				@Override
//				public void siteParsed() {
//					updateWidgetUI(fContext, fTown, updateViews);
//				}
//
//				@Override
//				public void error() {
//					//activityIndicator.setVisibility(View.INVISIBLE);
//
//				}
//			});




			return updateViews;
		}

		public void updateWidgetUI(Context context, Town town, RemoteViews updateViews) {
						
			
			
			try {
				if (town != null) {
					updateViews.setTextViewText(R.id.city, town.getTownName());
					//List<WebTimestep> ts = town.getWebTimesteps();
					//List<ForecastItem> ts = town.getForecastItems();

					//if (ts.size() >= 1) {

						//updateViews.setTextViewText(R.id.temp1, ""+ ts.get(0).temperature.c);

						//updateViews.setViewVisibility(R.id.image1, View.VISIBLE);
						//updateViews.setImageViewResource(R.id.image1, ForecastUIHelper.getCloudCoverImageId(context, (int) ts.get(0).cloud_cover.pct, ts.get(0).isDay()));

						//updateViews.setTextViewText(R.id.textView1, ts.get(1).getWeekDay());
						/*
						if (ts.get(0).getCloudImg() != null) {
							updateViews.setViewVisibility(R.id.image1, View.VISIBLE);
							updateViews.setImageViewResource(R.id.image1, getImageId(context, ts.get(0).getCloudImg() + "_dark"));
						} else {
							updateViews.setViewVisibility(R.id.image1, View.GONE);
						}*/
						/*
						if (ts.get(0).getFallingsImg() != null) {
							updateViews.setViewVisibility(R.id.rain_icon, View.VISIBLE);
							updateViews.setImageViewResource(R.id.rain_icon, getImageId(context, ts.get(0).getFallingsImg()));
						} else {
							updateViews.setViewVisibility(R.id.rain_icon, View.GONE);
						}*/
						//updateViews.setTextViewText(R.id.timeView1, " " + ts.get(1).getFormatedTime());

   
					//}
					
				}
				
			} catch (Exception ex) {
				Log.e("Weather", "Error: " + ex.getLocalizedMessage());
			}
		}

		public IBinder onBind(Intent intent) {
			// We don't need to bind to this service
			return null;
		}

		private void runOnUiThread(Runnable runnable) {
			handler.postDelayed(runnable, 100);

		}

		// Given a string representation of a URL, sets up a connection and gets
		// an input stream.
		private InputStream downloadUrl(String urlString) throws IOException {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			InputStream stream = conn.getInputStream();
			return stream;
		}
	}
	
	public static int getImageId(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

}
