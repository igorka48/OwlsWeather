package owlsdevelopers.com.owlsweather.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose


import java.io.File
import java.io.PrintWriter
import java.lang.reflect.Modifier


class DataManager {


    var weather: Weather? = null


    var townsMap: HashMap<String, Town> = HashMap()


    val towns: Array<Town>
        get() {
            return townsMap.values.toTypedArray()
        }


    @Transient val gson: Gson = GsonBuilder().create()


    init {
        this.weather = null
    }

//    val town: String
//        get() {
//            return this.weather?.point?.point_name ?: ""
//        }


    fun getTownByCode(code: String): Town? {
        return townsMap[code]
    }

    fun addTown(town: Town, checkUnique: Boolean) {
        townsMap.put(town.townCode, town)
    }


    fun removeTown(townCode: String) {
        if (townsMap.containsKey(townCode))
            townsMap.remove(townCode)
    }

    fun removeTown(townIndex: Int) {
        if (towns.size > townIndex)
            townsMap.remove(towns[townIndex].townCode)
    }


    fun save(context: Context) {
        try {
            var json = gson.toJson(this)
            Log.d("JSON", "json:" + json)
            val printWriter = PrintWriter(File(context.filesDir.toString() + serialFileName))

            printWriter.println(json)
            printWriter.close()
        } catch (ex: Exception) {
            Log.e("Weather", "Error:" + ex.message)
            ex.printStackTrace()
        } finally {
        }
    }


    companion object {
        val serialFileName = "/save.bin"
        fun load(context: Context): DataManager {
            try {
                val g = Gson()
                var file = File(context.filesDir.toString() + serialFileName)
                val text = file.readText()
                Log.d("OwlsWeatherApplication", "text " + text)
                val dataManager = g.fromJson(text, DataManager::class.java)
                Log.d("OwlsWeatherApplication", "DataManager restored from " + (context.filesDir.toString() + serialFileName))
                //Log.d("OwlsWeatherApplication", "Weather town: " + dataManager.town)
                return dataManager
            } catch (ex: Exception) {
                Log.e("OwlsWeatherApplication", "Error:" + ex.message)
                return DataManager()
            }
        }

    }

    fun invalidate() {
        towns.forEach { t -> t.lastUpdateTimestamp = 0}
    }

}