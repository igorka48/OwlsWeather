package owlsdevelopers.com.owlsweather.ui


import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.timestep_item.view.*
import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.ui.model.WeatherTimestep


class TimestepAdapter(val context: Context, data: List<WeatherTimestep>,
                      private val clickListener: TimestepClickListener) : RecyclerView.Adapter<TimestepAdapter.ViewHolder>() {



    var data: List<WeatherTimestep> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimestepAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.timestep_item, parent, false)
        val impact = Typeface.createFromAsset(context.assets, "Impact.ttf")
        val vh = ViewHolder(view)
        vh.itemView.timeTextView.typeface = impact
        vh.itemView.tempTextView.typeface = impact
        return vh
    }

    override fun onBindViewHolder(holder: TimestepAdapter.ViewHolder, position: Int) {
        val dayForecast = data[position]
        holder.bindForecast(dayForecast)
        holder.itemView.setOnClickListener { clickListener.itemClicked(position)

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindForecast(forecast: WeatherTimestep) {
            itemView.timeTextView.text = forecast.shortDate
            itemView.tempTextView.text = forecast.temperature
            itemView.weatherImage.setImageResource(forecast.cloudImgResId)
            itemView.rainImage.setImageResource(forecast.precipitationImgResId)
        }
    }


    interface TimestepClickListener {
        fun itemClicked(i: Int)
    }

}