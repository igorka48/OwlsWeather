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
import owlsdevelopers.com.owlsweather.ui.model.Town
import owlsdevelopers.com.owlsweather.ui.model.UnitSystem
import owlsdevelopers.com.owlsweather.ui.repository.SettingsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepositoryImp
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext
import javax.inject.Inject


class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var townsRepository: TownsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDepedency()

        setContentView(R.layout.activity_settings)
        initSpinners()
    }
    fun injectDepedency(){
        (applicationContext as OwlsWeatherApplication).applicationComponent?.inject(this)
    }
    fun initSpinners() {


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
                    townsRepository.invalidate()
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
                    townsRepository.invalidate()
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
                    townsRepository.invalidate()
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
