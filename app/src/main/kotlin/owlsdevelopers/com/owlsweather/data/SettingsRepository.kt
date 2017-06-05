package owlsdevelopers.com.owlsweather.data

import owlsdevelopers.com.owlsweather.data.model.UnitSystem

/**
 * Created by igorka on 6/3/17.
 */
interface SettingsRepository {

    fun getLanguageVariants():Array<String>

    fun getCurrentLanguage():String

    fun setCurrentLanguage(language: String) : Boolean

    fun getTowns():Array<Town>

    fun getTownForWidgets():Town?

    fun setTownForWidgets(town: Town) : Boolean

    fun getUnitSystemVariants():Array<UnitSystem>

    fun getCurrentUnitSystem():UnitSystem

    fun setCurrentUnitSystem(system: UnitSystem) : Boolean
}