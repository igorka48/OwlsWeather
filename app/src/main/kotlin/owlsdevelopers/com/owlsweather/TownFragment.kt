/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package owlsdevelopers.com.owlsweather

import android.content.Context
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.survivingwithandroid.weather.lib.model.DayForecast
import kotlinx.android.synthetic.main.fragment_town.*
import owlsdevelopers.com.owlsweather.data.FU
import owlsdevelopers.com.owlsweather.weatherlib.WeatherFragment
import java.util.*
import android.support.v7.widget.LinearLayoutManager



class TownFragment : WeatherFragment() {
    /**
     * The fragment's page number, which is set to the argument value for
     * [.ARG_PAGE].
     */
    /**
     * Returns the page number represented by this fragment object.
     */
    var pageNumber: Int = 0
        private set
    var url: String? = null
    private var rootView: ViewGroup? = null
    private var languageCode = "en"
    private val townCode: String? = null
    private val task: AsyncTask<*, *, *>? = null
    var forceUpdate: Boolean = false
    var broadcastReceiver: WeatherBroadcastReceiver? = null
    val adapter: TimestepAdapter = TimestepAdapter(ArrayList<DayForecast>(), object: TimestepAdapter.TimestepClickListener {
        override fun itemClicked(i: Int) {
            updateWebTimestepView(i)
        }
    })


    override fun refreshData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments.getInt(ARG_PAGE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater!!.inflate(R.layout.fragment_town,
                container, false) as ViewGroup
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        updateWebTimestepView(0)
        broadcastReceiver = WeatherBroadcastReceiver.sing(context, object : WeatherBroadcastReceiver.WeatherLoaderCallback {
            override fun loadingStart() {
                progressBar.visibility = View.VISIBLE
            }

            override fun loadingCompleted() {
                progressBar.visibility = View.GONE
                val dm = (context
                        .applicationContext as OwlsWeatherApplication).dataManager
                if (pageNumber >= dm.towns.size) {
                    return
                }
                val town = dm.towns[pageNumber]
                adapter.data = town.forecastItems
            }

        })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        loadWeather()
    }


    override fun onDestroy() {
        super.onDestroy()
        WeatherBroadcastReceiver.unsing(context, broadcastReceiver)
    }

    fun timeStepAction(v: View) {
        //int timestepNum = Integer.parseInt((String) v.getTag());
        //updateTimestepView(timestepNum);
    }

    //    public void rp5WebAction(View c) {
    //        Context context = null;
    //        context = c.getContext();
    //
    //        DataManager dm = ((OwlsWeatherApplication) context
    //                .getApplicationContext()).dataManager();
    //        Town town = dm.getTowns().get(mPageNumber);
    //        Intent browserIntent = new Intent(context, WebViewActivity.class);
    //        browserIntent.putExtra("URL", BROWSER_URL + town.getRp5Code() + "/"
    //                + languageCode);
    //        browserIntent.putExtra("TITLE",
    //                context.getResources().getString(R.string.weather) + " "
    //                        + town.getTownName());
    //        context.startActivity(browserIntent);
    //    }


    fun updateWebTimestepView(timestep: Int) {
        val dm = (context
                .applicationContext as OwlsWeatherApplication).dataManager
        if (pageNumber >= dm.towns?.size) {
            return
        }
        val town = dm.towns[pageNumber]


        townTitle.text = town.title

        //List<WebTimestep> timesteps = town.getWebTimesteps();
        val dayForecasts = town.forecastItems ?: return

        if (dayForecasts.size == 0) {
            return
        }
        if (dayForecasts.size <= timestep) {
            return
        }


        //WebTimestep ts = timesteps.get(timestep);
        val dayForecast = dayForecasts[timestep]


        val day = FU.isDay(Date(dayForecast.timestamp))
        var dispDate = ""

        if (day) {
            dispDate += context.resources.getString(R.string.day)
        } else {
            dispDate += context.resources.getString(R.string.night)
        }

        val date = rootView!!.findViewById(R.id.dateText) as TextView
        val temp = rootView!!.findViewById(R.id.tempText) as TextView
        val hum = rootView!!.findViewById(R.id.humidityText) as TextView
        val windDir = rootView!!
                .findViewById(R.id.windDirectionText) as TextView
        val windVel = rootView!!
                .findViewById(R.id.windVelocityText) as TextView
        //TextView rain = (TextView) rootView.findViewById(R.id.rainText);
        //TextView show = (TextView) rootView.findViewById(R.id.snowText);
        val press = rootView!!.findViewById(R.id.pressureText) as TextView
        val weatherIcon = rootView!!
                .findViewById(R.id.weather_icon) as ImageView
        val rainIcon = rootView!!.findViewById(R.id.rain_icon) as ImageView

        val impact = Typeface.createFromAsset(context.assets, "Impact.ttf")

        //Init fonts
        townTitle.typeface = impact
        temp.typeface = impact
        hum.typeface = impact
        windDir.typeface = impact
        windVel.typeface = impact
        date.typeface = impact
        press.typeface = impact


        //date.setText( "" + ts.getMonthDay() + ", " + ts.getFormatedTime());
        date.text = " " + FU.getDisplayLongDate(dayForecast.timestamp)
        temp.text = "" + dayForecast.forecastTemp.day + "" + context.resources.getString(R.string.temp_units)
        hum.text = "" + dayForecast.weather.currentCondition.getHumidity() + " "+ context.resources.getString(R.string.hum_units)
        // windDir.setText("" + ForecastUIHelper.getWindString(mActivity, (int) dayForecast.wind_direction));
        //windDir.setText("" + ts.wind_direction_hint);
        windVel.text = "" + dayForecast.weather.wind.getSpeed() + " "+ context.resources.getString(R.string.vind_units)
        press.text = "" + dayForecast.weather.currentCondition.getPressure() + " "+ context.resources.getString(R.string.press_units)


        // Log.d("Weather", "Clouds: " + dayForecast.cloud_cover);
        /*
        if (ts.getCloudImg() != null) {
			weatherIcon
					.setImageResource(getImageId(mActivity, ts.getCloudImg()));
		} else {
			weatherIcon.setImageDrawable(null);
		}*/
        //        weatherIcon.setImageResource(ForecastUIHelper.getCloudCoverImageId(mActivity, (int) dayForecast.cloud_cover.pct, day));
        //        if (dayForecast.precipitation.mm > 0) {
        //            rainIcon.setVisibility(View.VISIBLE);
        //            rainIcon.setImageResource(ForecastUIHelper.getPrecipitationImageId(mActivity, dayForecast.precipitation.mm, (int) dayForecast.precipitation_type));
        //        } else {
        //            rainIcon.setVisibility(View.GONE);
        //            rainIcon.setImageDrawable(null);
        //        }

        /*
        if (ts.getFallingsImg() != null) {
			rainIcon.setVisibility(View.VISIBLE);
			rainIcon.setImageResource(getImageId(mActivity, ts.getFallingsImg()));
		} else {
			rainIcon.setVisibility(View.GONE);
			rainIcon.setImageDrawable(null);
		}*/


    }

    //    public void updateTownView() {
    //
    //        if (mActivity == null) {
    //            return;
    //        }
    //
    //        DataManager dm = ((OwlsWeatherApplication) mActivity
    //                .getApplicationContext()).dataManager();
    //
    //        try {
    //            if ((dm.getTowns() != null) && (dm.getTowns().size() > mPageNumber)) {
    //                Town town = dm.getTowns().get(mPageNumber);
    //                //List<WebTimestep> ts = town.getWebTimesteps();
    //                List<DayForecast> ts = town.getForecastItems();
    //                HorizontalListView mHlvSimpleList = (HorizontalListView) rootView
    //                        .findViewById(R.id.timestepsList);
    //                TimestepArrayAdapter adapter = new TimestepArrayAdapter(
    //                        mActivity, ts.toArray(new ForecastItem[ts.size()]));
    //                mHlvSimpleList.setAdapter(adapter);
    //                adapter.selectedItem = 0;
    //
    //                adapter.notifyDataSetChanged();
    //
    //                mHlvSimpleList
    //                        .setOnItemClickListener(new OnItemClickListener() {
    //
    //                            @Override
    //                            public void onItemClick(AdapterView<?> arg0,
    //                                                    View arg1, int arg2, long arg3) {
    //                                updateWebTimestepView(arg2);
    //                                ((TimestepArrayAdapter) arg0.getAdapter()).selectedItem = arg2;
    //                                ((TimestepArrayAdapter) arg0.getAdapter()).notifyDataSetChanged();
    //                            }
    //
    //                        });
    //            }
    //        } catch (Exception ex) {
    //
    //        }
    //
    //    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
    }

    fun loadWeather() {
        val dm = (context.applicationContext.applicationContext as OwlsWeatherApplication).dataManager
        val town = dm.towns[pageNumber]
       WeatherRcvService.loadWeather(context, town.townCode)

//        if (mActivity == null) {
//            if (activity != null) {
//                mActivity = activity
//            } else {
//                return
//            }
//        }
//
//        val dm = (mActivity!!
//                .applicationContext as OwlsWeatherApplication).dataManager
//        val town = dm.towns[pageNumber]
//
//        val sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(mActivity)
//        languageCode = sharedPrefs.getString("langPref", mActivity!!
//                .resources.getString(R.string.defaultLanguage))
//
//
//        activityIndicator!!.visibility = View.VISIBLE
//
//
//        val req = WeatherRequest("2643743")
//        getWeatherClient().getForecastWeather(req, object : WeatherClient.ForecastWeatherEventListener {
//            override fun onWeatherRetrieved(forecast: WeatherForecast) {
//                forceUpdate = false
//                activityIndicator!!.visibility = View.INVISIBLE
//
//
//                //                if (isOld) {
//                //                    Date lastUpdate = town.getLastUpdate();
//                //                    if (lastUpdate != null) {
//                //                        Date current = new Date();
//                //                        long diff = current.getTime() - lastUpdate.getTime();
//                //                        long hours = TimeUnit.MILLISECONDS.toHours(diff);
//                //                        if (hours < 5) {
//                //                            return "ok";
//                //                        }
//                //                    }
//                //                }
//
//                town.lastUpdateTimestamp = System.currentTimeMillis()
//                town.forecast = forecast
//                // town.setTownName(weatherResponce.response.where);
//                // town.setTitle(forecast);
//                //                if (town.getForecastItems().size() > 10) {
//                //                    succsess = true;
//                //                }
//
//
//                val data = (context.applicationContext as OwlsWeatherApplication).dataManager
//                try {
//                    val oos = ObjectOutputStream(
//                            FileOutputStream(File(context
//                                    .filesDir.toString() + "/save.bin")))
//                    oos.writeObject(data) // write the class as an 'object'
//                    oos.flush() // flush the stream to insure all of the
//                    // information
//                    // was written to 'save.bin'
//                    oos.close()// close the stream
//                } catch (ex: Exception) {
//                    Log.e("Weather", "Error:" + ex.message)
//                    ex.printStackTrace()
//                    val articleParams = HashMap<String, String>()
//                    articleParams.put("error", "" + ex.message)
//                    // FlurryAgent.logEvent("ERROR", articleParams);
//                }
//
//                try {
//                    val man = AppWidgetManager.getInstance(mActivity)
//                    val ids = man.getAppWidgetIds(ComponentName(mActivity,
//                            WidgetSmall::class.java))
//                    val updateIntent = Intent()
//                    updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                    updateIntent.putExtra(WidgetSmall.WIDGET_IDS_KEY, ids)
//                    mActivity!!.sendBroadcast(updateIntent)
//
//                    val idsB = man.getAppWidgetIds(ComponentName(mActivity,
//                            WidgetBig::class.java))
//                    val updateIntentB = Intent()
//                    updateIntentB.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                    updateIntentB.putExtra(WidgetSmall.WIDGET_IDS_KEY, idsB)
//                    mActivity!!.sendBroadcast(updateIntentB)
//
//                } catch (ex: Exception) {
//
//                }
//
//                updateWebTimestepView(0)
//                //updateTownView();
//            }
//
//            override fun onWeatherError(wle: WeatherLibException) {
//                activityIndicator!!.visibility = View.INVISIBLE
//            }
//
//            override fun onConnectionError(t: Throwable) {
//                activityIndicator!!.visibility = View.INVISIBLE
//            }
//        })


        //		Rp5SiteParser parser = new Rp5SiteParser(mActivity, town, languageCode, !forceUpdate);
        //		parser.parse(new Rp5SiteParserCallBack() {
        //
        //			@Override
        //			public void siteParsed() {
        //				forceUpdate = false;
        //				activityIndicator.setVisibility(View.INVISIBLE);
        //
        //				try {
        //					AppWidgetManager man = AppWidgetManager.getInstance(mActivity);
        //					int[] ids = man.getAppWidgetIds(new ComponentName(mActivity,
        //							WidgetSmall.class));
        //					Intent updateIntent = new Intent();
        //					updateIntent
        //							.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //					updateIntent.putExtra(WidgetSmall.WIDGET_IDS_KEY, ids);
        //					mActivity.sendBroadcast(updateIntent);
        //
        //					int[] idsB = man.getAppWidgetIds(new ComponentName(mActivity,
        //							WidgetBig.class));
        //					Intent updateIntentB = new Intent();
        //					updateIntentB
        //							.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //					updateIntentB.putExtra(WidgetSmall.WIDGET_IDS_KEY, idsB);
        //					mActivity.sendBroadcast(updateIntentB);
        //
        //				} catch (Exception ex) {
        //
        //				}
        //
        //				updateWebTimestepView(0);
        //				updateTownView();
        //
        //			}
        //
        //			@Override
        //			public void error() {
        //				activityIndicator.setVisibility(View.INVISIBLE);
        //
        //			}
        //		});

    }


    companion object {

        // private ParallaxView paralax;

        /**
         * The argument key for the page number this fragment represents.
         */
        val ARG_PAGE = "page"

        fun create(pageNumber: Int): TownFragment {
            val fragment = TownFragment()
            //fragment.setRetainInstance(true);
            val args = Bundle()
            args.putInt(ARG_PAGE, pageNumber)
            fragment.arguments = args
            fragment.forceUpdate = false
            // fragment.setUrl(url);
            return fragment
        }


        fun getImageId(context: Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable",
                    context.packageName)
        }

        fun isNumeric(str: String): Boolean {
            try {
                val d = Integer.parseInt(str).toDouble()
            } catch (nfe: NumberFormatException) {
                return false
            }

            return true
        }
    }
}
