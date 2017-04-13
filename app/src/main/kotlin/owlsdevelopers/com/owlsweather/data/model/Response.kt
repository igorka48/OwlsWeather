package owlsdevelopers.com.owlsweather.data.model

import java.io.Serializable
import java.util.Date

/**
 * Created by admin on 27.06.15.
 */
class Response : Serializable {

    var created: Date? = null
    var local_time: Date? = null
    var where: String? = null
    var where_unique: String? = null
    var forecast: Forecast? = null

    companion object {

        private const val serialVersionUID = 1L
    }

}
