package owlsdevelopers.com.owlsweather.data.mapper

import android.content.Context
import android.content.res.TypedArray
import owlsdevelopers.com.owlsweather.R

object WeatherIconMapper {

    fun getCloudResource(context: Context, id: String): Int {

        val img: TypedArray
        val day = id.endsWith("d", true)
        if (day) {
            img = context.resources.obtainTypedArray(R.array.forecast_cloud_cover_day)
        } else {
            img = context.resources.obtainTypedArray(R.array.forecast_cloud_cover_night)
        }
        val iconCode = id.substring(0, 1).toInt()
        when(iconCode){
            1 -> return img.getResourceId(0, -1)
            2, 10 -> return img.getResourceId(1, -1)
            3, 11 -> return img.getResourceId(2, -1)
            4, 12 -> return img.getResourceId(3, -1)
            13 -> return img.getResourceId(3, -1)
            50 -> return img.getResourceId(3, -1)
        }


        return R.drawable.ic_forecast_cloud_cover_day_4

    }

    fun getPrecipitationResource(context: Context, id: String): Int {

        val iconCode = id.substring(0, 1).toInt()
        val img = context.resources.obtainTypedArray(R.array.forecast_rain)


        when(iconCode){
            9 -> return img.getResourceId(0, -1)
            10 -> return img.getResourceId(1, -1)
            11 -> return img.getResourceId(2, -1)
            12 -> return img.getResourceId(4, -1)
            13 -> return R.drawable.ic_forecast_snow_4
            50 -> return R.drawable.ic_forecast_no_precipitation
        }


        return R.drawable.ic_forecast_no_precipitation

    }

}