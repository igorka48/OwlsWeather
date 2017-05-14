package owlsdevelopers.com.owlsweather.data.mapper

import android.content.Context
import owlsdevelopers.com.owlsweather.MoonPhase
import owlsdevelopers.com.owlsweather.R
import java.util.*

object MoonIconMapper {
    fun getMoonResource(context: Context, timestamp: Long): Int {
        val img = context.resources.obtainTypedArray(R.array.forecast_moon)
        val calendar = Calendar.getInstance()
        calendar.time = Date(timestamp)
        val phase = MoonPhase.computePhaseIndex(calendar)
        return img.getResourceId(phase, -1)
    }
}