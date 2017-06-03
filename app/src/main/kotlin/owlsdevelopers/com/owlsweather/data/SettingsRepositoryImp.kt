package owlsdevelopers.com.owlsweather.data

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by igorka on 6/3/17.
 */
class SettingsRepositoryImp(val context: Context, val dataManager: DataManager) : SettingsRepository {
    override fun getLanguageVariants(): List<String> {
        return listOf("ru", "en", "de", "uk", "es", "fr")
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

    override fun getTowns(): List<Town> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTownForWidgets(): Town {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTownForWidgets(town: Town): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUnitSystemVariants(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentUnitSystem(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCurrentUnitSystem(system: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private val LANG_PREF_KEY = "currentLanguagCodePref"
        private val LANG_DEFAULT_VALUE = "en"
        private val TOWN_PREF_KEY = "owlsdevelopers.com.owlsweather.extra.CITY_ID"
        private val UNITS_PREF_KEY = "owlsdevelopers.com.owlsweather.extra.CITY_ID"
    }
}