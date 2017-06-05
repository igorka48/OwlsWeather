/*
 * Copyright (C) 2014 Francesco Azzola
 *  Surviving with Android (http://www.survivingwithandroid.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * This is a tutorial source code
 * provided "as is" and without warranties.
 *
 *
 * For any question please visit the web site
 * http://www.survivingwithandroid.com
 *
 *
 * or write an email to
 * survivingwithandroid@gmail.com
 */
package owlsdevelopers.com.owlsweather


import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import com.survivingwithandroid.weather.lib.WeatherClient
import com.survivingwithandroid.weather.lib.exception.LocationProviderNotFoundException
import com.survivingwithandroid.weather.lib.exception.WeatherLibException
import com.survivingwithandroid.weather.lib.model.City
import kotlinx.android.synthetic.main.search_location_activity.*
import owlsdevelopers.com.owlsweather.data.Town
import owlsdevelopers.com.owlsweather.weatherlib.CityAdapter
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*


/**
 * @author Francesco
 */
@RuntimePermissions
class AddTownActivity : Activity() {


    private var adp: CityAdapter? = null
    private var client: WeatherClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_location_activity)
        val data = (applicationContext as OwlsWeatherApplication).dataManager
        client = WeatherContext.getInstance().getClient(this, data)

       // Log.d("App", "Client [$client]")

        //cityList = findViewById(R.id.cityList) as ListView
        //bar = findViewById(R.id.progressBar2) as ProgressBar
        adp = CityAdapter(this, ArrayList<City>())
        cityList?.adapter = adp


        cityEdtText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(v.text.toString())
                return@OnEditorActionListener true
            }

            false
        })

        imgSearch.setOnClickListener { search(cityEdtText.editableText.toString()) }

        cityList?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            val city = parent.getItemAtPosition(pos) as City

            //TODO REMOVE THIS INTO RCV_SERVICE
            val sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this)
            val editor = sharedPrefs.edit()
            editor.putString("townCode", "" + city.id)
            editor.commit()

            val dm = (applicationContext as OwlsWeatherApplication).dataManager
            Log.d("Weather", "Towns count: " + dm.towns.size)
            val town = Town(townName = city.name, townCode = city.id)
            town.title = city.name
            dm.addTown(town, true)
            dm.save(this)

           // Log.d("Weather", "Town added: " + city.id)
           // Log.d("Weather", "Towns count: " + dm.towns.size)

            if (dm.towns.size == 1) {
                editor.putString("widgetTownCodePref", "" + city.id)
                editor.commit()
            }


            finish()
        }

        imgLocationSearch.setOnClickListener {
            AddTownActivityPermissionsDispatcher.searchByLocationWithCheck(this)
        }

        AddTownActivityPermissionsDispatcher.searchByLocationWithCheck(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AddTownActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun searchByLocation() {
        progressBar?.visibility = View.VISIBLE
        try {
            client!!.searchCityByLocation(WeatherClient.createDefaultCriteria(), object : WeatherClient.CityEventListener {

                override fun onCityListRetrieved(cityList: List<City>) {
                    progressBar?.visibility = View.GONE
                    adp!!.setCityList(cityList)
                    adp!!.notifyDataSetChanged()
                }

                override fun onWeatherError(wle: WeatherLibException) {
                    progressBar?.visibility = View.GONE
                }

                override fun onConnectionError(t: Throwable) {
                    progressBar?.visibility = View.GONE
                }
            })
        } catch (lpnfe: LocationProviderNotFoundException) {

        }
    }

    private fun search(pattern: String) {
        progressBar?.visibility = View.VISIBLE
        client?.searchCity(pattern, object : WeatherClient.CityEventListener {
            override fun onCityListRetrieved(cityList: List<City>) {
                progressBar?.visibility = View.GONE
                adp?.setCityList(cityList)
                adp?.notifyDataSetChanged()
            }

            override fun onWeatherError(t: WeatherLibException) {
                progressBar?.visibility = View.GONE
            }

            override fun onConnectionError(t: Throwable) {
                progressBar?.visibility = View.GONE
            }
        })
    }

}
