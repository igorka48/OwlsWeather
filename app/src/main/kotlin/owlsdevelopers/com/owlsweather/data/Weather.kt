package owlsdevelopers.com.owlsweather.data

import java.io.Serializable


class Weather : Serializable {

    var created: String? = null

    var date: String? = null

    var point: Point? = null

    companion object {

        private const val serialVersionUID = 1L
    }

}
