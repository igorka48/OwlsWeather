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

package owlsdevelopers.com.owlsweather.ui

import android.content.Context
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_town.*
import java.util.*
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.WeatherBroadcastReceiver
import owlsdevelopers.com.owlsweather.WeatherRcvService
import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.ui.model.WeatherTimestep
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepositoryImp
import owlsdevelopers.com.owlsweather.util.FU
import javax.inject.Inject


class TownFragment : Fragment() {
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

    @Inject
    lateinit var data: DataManager

    @Inject
    lateinit var townsRepository: TownsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments.getInt(ARG_PAGE)
        injectDepedency()
    }

    fun injectDepedency(){
        (context.applicationContext as OwlsWeatherApplication).applicationComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater!!.inflate(R.layout.fragment_town,
                container, false) as ViewGroup
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val adapter: TimestepAdapter = TimestepAdapter(context, arrayOf<WeatherTimestep>(), object : TimestepAdapter.TimestepClickListener {
            override fun itemClicked(i: Int) {
                updateWebTimestepView(i)
            }
        })
        initViewStyle()
        updateWebTimestepView(0)
        broadcastReceiver = WeatherBroadcastReceiver.sign(context, object : WeatherBroadcastReceiver.WeatherLoaderCallback {
            override fun loadingError(message: String) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            override fun loadingStart() {
                progressBar.visibility = View.VISIBLE
            }

            override fun loadingCompleted() {
                progressBar.visibility = View.GONE

                if (pageNumber >= townsRepository.getTowns().size) {
                    return
                }
                val town = townsRepository.getTowns()[pageNumber]
                adapter.data = town.forecast
            }

        })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        loadWeather()
    }


    override fun onDestroy() {
        super.onDestroy()
        WeatherBroadcastReceiver.unsign(context, broadcastReceiver)
    }


    fun initViewStyle() {
        val impact = Typeface.createFromAsset(context.assets, "Impact.ttf")

        //Init fonts
        townTitle.typeface = impact
        tempText.typeface = impact
        humidityText.typeface = impact
        windDirectionText.typeface = impact
        windVelocityText.typeface = impact
        dateText.typeface = impact
        pressureText.typeface = impact
        tempRealText.typeface = impact
        sunsetText.typeface = impact
    }


    fun updateWebTimestepView(timestep: Int) {
        if (pageNumber >= townsRepository.getTowns().size) {
            return
        }
        val town = townsRepository.getTowns()[pageNumber]


        townTitle.text = town.title

        val dayForecasts = town.forecast

        if (dayForecasts.size == 0) {
            return
        }
        if (dayForecasts.size <= timestep) {
            return
        }


        val dayForecast = dayForecasts[timestep]


        val day = FU.isDay(Date(dayForecast.timestep))
        var dispDate = ""

        if (day) {
            dispDate += context.resources.getString(R.string.day)
        } else {
            dispDate += context.resources.getString(R.string.night)
        }


        //TextView rain = (TextView) rootView.findViewById(R.id.rainText);
        //TextView show = (TextView) rootView.findViewById(R.id.snowText);
        val weatherIcon = rootView!!
                .findViewById(R.id.weather_icon) as ImageView
        val rainIcon = rootView!!.findViewById(R.id.rain_icon) as ImageView


        dateText.text = dayForecast.longDate
        tempText.text = "" + dayForecast.temperature /* + "" + context.resources.getString(R.string.temp_units)*/
        humidityText.text = "" + dayForecast.humidity  /* + " "+ context.resources.getString(R.string.hum_units) */
        windVelocityText.text = "" + dayForecast.windVelocity /*+ " "+ context.resources.getString(R.string.vind_units)*/
        windDirectionText.text = dayForecast.windDirection
        pressureText.text = "" + dayForecast.pressure /*+ " "+ context.resources.getString(R.string.press_units)*/
        tempRealText.text = dayForecast.realFeel
        weatherIcon.setImageResource(dayForecast.cloudImgResId)
        rainIcon.setImageResource(dayForecast.precipitationImgResId)
        moonText.setCompoundDrawablesRelativeWithIntrinsicBounds(dayForecast.moonImgResId, 0, 0, 0)
        sunsetText.text = dayForecast.sunriceSunset

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
    }

    fun loadWeather() {
        val town = townsRepository.getTowns()[pageNumber]
        WeatherRcvService.loadWeather(context, town.townCode, false)
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
