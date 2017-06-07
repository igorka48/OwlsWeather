package owlsdevelopers.com.owlsweather.data.repository

import android.content.Context
import android.preference.PreferenceManager
import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.data.model.Town
import owlsdevelopers.com.owlsweather.data.model.UnitSystem
import owlsdevelopers.com.owlsweather.ui.repository.SettingsRepository
import owlsdevelopers.com.owlsweather.weatherlib.WeatherLibUnitSystem

/**
 * Created by igorka on 6/3/17.
 */
class SettingsRepositoryImp(val context: Context, val dataManager: DataManager) : SettingsRepository {
    override fun getLanguageVariants(): Array<String> {
        return arrayOf("ru", "en", "de", "uk", "es", "fr")
    }

    override fun getCurrentLanguage(): String {
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val curLang = sharedPrefs.getString(LANG_PREF_KEY, LANG_DEFAULT_VALUE)
        return curLang
    }

    override fun setCurrentLanguage(language: String): Boolean {
        val oldValue = getCurrentLanguage()
        if (language.contentEquals(oldValue)) {
            return false
        }
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString(LANG_PREF_KEY, language)
        editor.commit()
        return true
    }

    override fun getTowns(): Array<Town> {
        return dataManager.towns
    }

    override fun getTownForWidgets(): Town? {
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val townId = sharedPrefs.getString(TOWN_PREF_KEY, "")
        if (townId.isEmpty())return null
        return dataManager.getTownByCode(townId)
    }

    override fun setTownForWidgets(town: Town): Boolean {
        val oldValue = getTownForWidgets()
        if (oldValue?.townCode.equals(town.townCode, true)) {
            return false
        }
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString(TOWN_PREF_KEY, town.townCode)
        editor.commit()
        return true
    }

    override fun getUnitSystemVariants(): Array<UnitSystem> {
        return arrayOf(WeatherLibUnitSystem.Metric(), WeatherLibUnitSystem.Imperial())
    }

    override fun getCurrentUnitSystem(): UnitSystem {
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val unitId = sharedPrefs.getString(UNITS_PREF_KEY, "")
        return WeatherLibUnitSystem.unitSystemById(unitId)
    }

    override fun setCurrentUnitSystem(system: UnitSystem): Boolean {
        val oldValue = getCurrentUnitSystem()
        if (oldValue.key.contentEquals(system.key)) {
            return false
        }
        val sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString(UNITS_PREF_KEY, system.key)
        editor.commit()
        return true
    }


    companion object {
        private val LANG_PREF_KEY = "currentLangugeCodePref"
        private val LANG_DEFAULT_VALUE = "en"
        private val TOWN_PREF_KEY = "townForWidgetsPref"
        private val UNITS_PREF_KEY = "currentUnitSystemPref"
    }
}