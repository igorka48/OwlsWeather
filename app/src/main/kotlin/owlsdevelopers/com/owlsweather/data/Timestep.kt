package owlsdevelopers.com.owlsweather.data

import java.io.Serializable


class Timestep : Serializable {

    var time_step: Int = 0

    var datetime: String? = null

    var g: Int = 0

    var hHii: String? = null

    var cloud_cover: Int = 0

    var precipitation: Float = 0.toFloat()

    var pressure: Int = 0

    var temperature: Int = 0

    var humidity: Int = 0

    var wind_direction: String? = null

    var wind_velocity: Int = 0

    var falls: Int = 0

    var drops: Float = 0.toFloat()
        private set

    fun setDrops(drops: Int) {
        this.drops = drops.toFloat()
    }

    companion object {

        private const val serialVersionUID = 1L
    }


}
