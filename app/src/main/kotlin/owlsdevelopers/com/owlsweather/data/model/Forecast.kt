package owlsdevelopers.com.owlsweather.data.model

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by admin on 27.06.15.
 */
class Forecast : Serializable {

    var items: List<List<ForecastItem>>? = null

    val allItems: List<ForecastItem>
        get() {
            val list = ArrayList<ForecastItem>()
            for (outerList in items!!) {
                for (fi in outerList) {
                    list.add(fi)
                }
            }

            return list
        }


}
