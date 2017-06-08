package owlsdevelopers.com.owlsweather.ui.repository

import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.ui.model.Town

/**
 * Created by igorka on 6/8/17.
 */
 class TownsRepositoryImp(val dataManager: DataManager): TownsRepository() {
    override fun addTown(town: Town, checkUnique: Boolean) {
        dataManager.townsMap.put(town.townCode, town)
    }

    override fun removeTown(townCode: String) {
        if (dataManager.townsMap.containsKey(townCode)) {
            dataManager.townsMap.remove(townCode)
        }
    }

    override fun removeTown(townIndex: Int) {
        if (getTowns().size > townIndex)
            dataManager.townsMap.remove(getTowns()[townIndex].townCode)
    }

    override fun getTownByCode(code: String): Town? {
        return dataManager.townsMap[code]
    }

    override fun getTowns():Array<Town> {
         return dataManager.townsMap.values.toTypedArray()
     }

    override fun invalidate() {
        getTowns().forEach { t -> t.lastUpdateTimestamp = 0}
    }
}

