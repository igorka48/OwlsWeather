package owlsdevelopers.com.owlsweather.ui


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
import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.ui.model.Town
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepositoryImp
import owlsdevelopers.com.owlsweather.weatherlib.CityAdapter
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.util.*
import javax.inject.Inject


@RuntimePermissions
class AddTownActivity : Activity() {


    private var adp: CityAdapter? = null

    @Inject
    lateinit var data: DataManager
    @Inject
    lateinit var townsRepository: TownsRepository
    @Inject
    lateinit var client: WeatherClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDepedency()
        setContentView(R.layout.search_location_activity)

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

            val town = Town(townName = city.name, townCode = city.id)
            town.title = city.name
            townsRepository.addTown(town, true)
            data.save(this)

           // Log.d("Weather", "Town added: " + city.id)
           // Log.d("Weather", "Towns count: " + dm.towns.size)

            if (townsRepository.getTowns().size == 1) {
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

    fun injectDepedency(){
        (applicationContext as OwlsWeatherApplication).applicationComponent?.inject(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AddTownActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun searchByLocation() {
        progressBar?.visibility = View.VISIBLE
        try {
            client.searchCityByLocation(WeatherClient.createDefaultCriteria(), object : WeatherClient.CityEventListener {

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
        client.searchCity(pattern, object : WeatherClient.CityEventListener {
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
