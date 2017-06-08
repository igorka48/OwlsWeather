package owlsdevelopers.com.owlsweather.ui.repository

import owlsdevelopers.com.owlsweather.ui.model.Town

/**
 * Created by igorka on 6/8/17.
 */
abstract class TownsRepository{
    abstract fun getTowns():Array<Town>
    abstract fun getTownByCode(code: String): Town?
    abstract fun addTown(town: Town, checkUnique: Boolean)
    abstract fun removeTown(townCode: String)
    abstract fun removeTown(townIndex: Int)
    abstract fun invalidate()
}
