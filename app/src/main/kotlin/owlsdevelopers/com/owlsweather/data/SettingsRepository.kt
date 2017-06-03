package owlsdevelopers.com.owlsweather.data

/**
 * Created by igorka on 6/3/17.
 */
interface SettingsRepository {

    fun getLanguageVariants():List<String>

    fun getCurrentLanguage():String

    fun setCurrentLanguage(language: String) : Boolean

    fun getTowns():List<Town>

    fun getTownForWidgets():Town

    fun setTownForWidgets(town: Town) : Boolean

    fun getUnitSystemVariants():List<String>

    fun getCurrentUnitSystem():String

    fun setCurrentUnitSystem(system: String) : Boolean
}