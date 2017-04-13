package owlsdevelopers.com.owlsweather.data

import android.content.Context
import android.content.res.TypedArray

import owlsdevelopers.com.owlsweather.R

/**
 * Created by admin on 27.06.15.
 */
object ForecastUIHelper {

    fun getWindString(context: Context, value: Int): String {
        val wind = context.resources.getStringArray(R.array.wind_direction_text_forecast)

        when (value) {
            361 -> return wind[0]
            360 -> return wind[1]
            359 -> return wind[2]
            366 -> return wind[3]
            365 -> return wind[4]
            364 -> return wind[5]
            363 -> return wind[6]
            362 -> return wind[7]
            else -> return wind[8]
        }

    }

    fun getCloudCoverImageId(context: Context, value: Int, day: Boolean): Int {

        val img: TypedArray
        if (day) {
            img = context.resources.obtainTypedArray(R.array.forecast_cloud_cover_day)
        } else {
            img = context.resources.obtainTypedArray(R.array.forecast_cloud_cover_night)
        }

        if (value >= 99) {
            return img.getResourceId(7, -1)
        }
        if (value >= 80) {
            return img.getResourceId(6, -1)
        }
        if (value >= 70) {
            return img.getResourceId(5, -1)
        }
        if (value >= 50) {
            return img.getResourceId(4, -1)
        }
        if (value >= 30) {
            return img.getResourceId(3, -1)
        }
        if (value >= 20) {
            return img.getResourceId(2, -1)
        }
        if (value > 0) {
            return img.getResourceId(1, -1)
        } else {
            return img.getResourceId(0, -1)
        }


    }

    fun getPrecipitationImageId(context: Context, value: Double, type: Int): Int {
        if (value <= 0) {
            return R.drawable.ic_forecast_no_precipitation
        }
        val img: TypedArray
        when (type) {
            1 -> img = context.resources.obtainTypedArray(R.array.forecast_rain)
            2 -> img = context.resources.obtainTypedArray(R.array.forecast_snow)
            3 -> img = context.resources.obtainTypedArray(R.array.forecast_rain_and_snow)
            else -> img = context.resources.obtainTypedArray(R.array.forecast_rain)
        }

        if (value < 1.1) {
            return img.getResourceId(0, -1)
        }
        if (value < 3.1) {
            return img.getResourceId(1, -1)
        }
        if (value < 6.1) {
            return img.getResourceId(2, -1)
        }
        if (value < 24.8) {
            return img.getResourceId(3, -1)
        }

        return img.getResourceId(4, -1)


    }

    fun getImageId(context: Context, name: String): Int {
        return context.resources.getIdentifier(name, "drawable",
                context.packageName)
    }


}
