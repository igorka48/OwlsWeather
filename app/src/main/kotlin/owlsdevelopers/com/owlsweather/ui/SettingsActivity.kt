package owlsdevelopers.com.owlsweather.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.ArrayAdapter
import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.data.repository.SettingsRepositoryImp
import owlsdevelopers.com.owlsweather.data.model.Town
import owlsdevelopers.com.owlsweather.data.model.UnitSystem
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initSpinners()
    }

    fun initSpinners() {
        //Needs di
        val data = (applicationContext as OwlsWeatherApplication).dataManager
        val settingsRepository = SettingsRepositoryImp(this, data)

        spinnerLang.adapter  = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                settingsRepository.getLanguageVariants())
        spinnerLang.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
              val succsess = settingsRepository.setCurrentLanguage(settingsRepository.getLanguageVariants()[i])
                if(succsess) {
                    WeatherContext.getInstance().invalidate()
                    data.invalidate()
                }
            }
        }
        spinnerUnits.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                unitSystemsToStringArray(settingsRepository.getUnitSystemVariants()))
        spinnerUnits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                val succsess = settingsRepository.setCurrentUnitSystem(settingsRepository.getUnitSystemVariants()[i])
                if(succsess) {
                    WeatherContext.getInstance().invalidate()
                    data.invalidate()
                }
            }
        }
        spinnerTown.adapter  = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                townsToStringArray(settingsRepository.getTowns()))
        spinnerTown.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                val succsess = settingsRepository.setTownForWidgets(settingsRepository.getTowns()[i])
                if(succsess) {
                    WeatherContext.getInstance().invalidate()
                    data.invalidate()
                }
            }
        }
    }

    fun townsToStringArray(towns: Array<Town>):Array<String>{
        return towns.map { town -> town.townName }.toTypedArray()
    }

    fun unitSystemsToStringArray(units: Array<UnitSystem>):Array<String>{
        return units.map { unitSystem -> unitSystem.dsc  }.toTypedArray()
    }


}
