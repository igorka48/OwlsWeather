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

import com.survivingwithandroid.weather.lib.model.DayForecast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import owlsdevelopers.com.owlsweather.data.DataManager;
import owlsdevelopers.com.owlsweather.data.Town;


public class WidgetBig extends AppWidgetProvider {

	final String LOG_TAG = "myLogs";

	public static final String WIDGET_IDS_KEY = "mybigwidgetproviderwidgetids";

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
			ComponentName thisWidget = new ComponentName(this, WidgetBig.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);
		}

		public RemoteViews buildUpdate(Context context) {
			handler = new Handler();
			Intent intent = new Intent(context, HomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			final RemoteViews updateViews = new RemoteViews(
					context.getPackageName(), R.layout.widget_big);
			
		
			updateViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			final String languageCode = sharedPrefs.getString("langPref",
					getResources().getString(R.string.defaultLanguage));

			final String townCode = sharedPrefs.getString("widgetTownCodePref", "");

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
//					List <DayForecast> ts = town.getForecastItems();
//
//					if (ts.size() > 1) {
//
//						updateViews.setTextViewText(R.id.temp1, ""
//								+ ts.get(1).temperature.c);
//						updateViews.setTextViewText(R.id.textView1, ts.get(1).getWeekDay());
//
//
//						updateViews.setViewVisibility(R.id.image1, View.VISIBLE);
//						updateViews.setImageViewResource(R.id.image1, ForecastUIHelper.getCloudCoverImageId(context, (int) ts.get(1).cloud_cover.pct, ts.get(1).isDay()));
//
//						updateViews.setTextViewText(R.id.timeView1, " " + ts.get(1).getFormatedTime());
//
//
//					}
//
//					if (ts.size() > 3) {
//						updateViews.setTextViewText(R.id.temp2, ""
//								+ ts.get(3).temperature.c);
//						updateViews.setTextViewText(R.id.textView2, ts.get(3).getWeekDay());
//
//
//						updateViews.setViewVisibility(R.id.image2, View.VISIBLE);
//						updateViews.setImageViewResource(R.id.image2, ForecastUIHelper.getCloudCoverImageId(context, (int) ts.get(3).cloud_cover.pct, ts.get(3).isDay()));
//
//						updateViews.setTextViewText(R.id.timeView2, " " + ts.get(3).getFormatedTime());
//
//					}
//					if (ts.size() > 5) {
//						updateViews.setTextViewText(R.id.temp3, ""
//								+ ts.get(5).temperature.c);
//						updateViews.setTextViewText(R.id.textView3, ts.get(5).getWeekDay());
//
//
//						updateViews.setViewVisibility(R.id.image3, View.VISIBLE);
//						updateViews.setImageViewResource(R.id.image3, ForecastUIHelper.getCloudCoverImageId(context, (int) ts.get(5).cloud_cover.pct, ts.get(5).isDay()));
//
//
//						updateViews.setTextViewText(R.id.timeView3, " " + ts.get(5).getFormatedTime());
//
//					}
//					if (ts.size() > 7) {
//						updateViews.setTextViewText(R.id.temp4, ""
//								+ ts.get(7).temperature.c);
//						updateViews.setTextViewText(R.id.textView4, ts.get(7).getWeekDay());
//
//						updateViews.setViewVisibility(R.id.image4, View.VISIBLE);
//						updateViews.setImageViewResource(R.id.image4, ForecastUIHelper.getCloudCoverImageId(context, (int) ts.get(7).cloud_cover.pct, ts.get(7).isDay()));
//						/*
//						if (ts.get(7).getCloudImg() != null) {
//							updateViews.setViewVisibility(R.id.image4, View.VISIBLE);
//							updateViews.setImageViewResource(R.id.image4, getImageId(context, ts.get(7).getCloudImg()));
//						} else {
//							updateViews.setViewVisibility(R.id.image4, View.GONE);
//						}*/
//
//
//						updateViews.setTextViewText(R.id.timeView4, " " + ts.get(7).getFormatedTime());
//
//					}
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
