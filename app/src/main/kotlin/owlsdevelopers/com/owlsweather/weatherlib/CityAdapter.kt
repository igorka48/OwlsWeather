/*
 * Copyright (C) 2014 Francesco Azzola
 *  Surviving with Android (http://www.survivingwithandroid.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * This is a tutorial source code
 * provided "as is" and without warranties.

 * For any question please visit the web site
 * http://www.survivingwithandroid.com

 * or write an email to
 * survivingwithandroid@gmail.com

 */
package owlsdevelopers.com.owlsweather.weatherlib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.survivingwithandroid.weather.lib.model.City
import owlsdevelopers.com.owlsweather.R

/**
 * @author Francesco
 */
class CityAdapter(private val ctx: Context, private var cityList: List<City>?) : ArrayAdapter<City>(ctx, R.layout.row_city_layout, cityList) {


    override fun getCount(): Int {
        return cityList!!.size
    }


    override fun getItem(position: Int): City? {

        return cityList!![position]
    }


    override fun getItemId(position: Int): Long {
        val city = cityList!![position]
        return city.id.hashCode().toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.row_city_layout, parent, false)
        }

        val city = cityList!![position]
        val cityText = convertView!!.findViewById(R.id.cityName) as TextView
        cityText.text = city.name + "," + city.country

        return convertView
    }

    fun setCityList(cityList: List<City>) {
        this.cityList = cityList
    }

}